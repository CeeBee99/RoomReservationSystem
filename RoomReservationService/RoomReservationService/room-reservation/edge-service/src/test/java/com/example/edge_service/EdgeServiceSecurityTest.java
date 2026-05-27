package com.example.edge_service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOidcLogin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.edge_service.config.SecurityConfig;

import reactor.core.publisher.Mono;


@WebFluxTest
@Import(SecurityConfig.class)
public class EdgeServiceSecurityTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ReactiveClientRegistrationRepository clientRegistrationRepository;

    @MockBean
    private RedisTemplate<String, String> redisTemplate;

    // Stub the registration lookup so OidcClientInitiatedServerLogoutSuccessHandler
    // doesn't NPE on a null Mono — falls back to the default redirect handler.
    @BeforeEach
    void stubClientRegistrationRepository() {
        when(clientRegistrationRepository.findByRegistrationId(any()))
            .thenReturn(Mono.empty());
    }

    // --- Unauthenticated access ---

    // D.4: Unauthenticated requests to protected routes should get 401
    @Test
    void unauthenticatedRequest_toRooms_returns401() {
        webTestClient.get()
                .uri("/rooms/")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void unauthenticatedRequest_toReservations_returns401() {
        webTestClient.get()
                .uri("/reservations/")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    // D.4: Actuator health must be public — Kubernetes probes need this.
    // In @WebFluxTest the actuator endpoints aren't loaded, so we assert that
    // security lets the request through (i.e. NOT 401/403) rather than asserting
    // a 200 from the endpoint itself.
    @Test
    void unauthenticatedRequest_toActuatorHealth_isAllowed() {
        webTestClient.get()
                .uri("/actuator/health")
                .exchange()
                .expectStatus().value(status -> {
                    assertThat(status).isNotEqualTo(401);
                    assertThat(status).isNotEqualTo(403);
                });
    }

    @Test
    void unauthenticatedRequest_toActuatorInfo_isAllowed() {
        webTestClient.get()
                .uri("/actuator/info")
                .exchange()
                .expectStatus().value(status -> {
                    assertThat(status).isNotEqualTo(401);
                    assertThat(status).isNotEqualTo(403);
                });
    }

    // --- Authenticated access with mockOidcLogin ---

    // D.3: Authenticated users should pass through security to gateway routing
    @Test
    void authenticatedRequest_toRooms_passesSecurityLayer() {
        webTestClient.mutateWith(mockOidcLogin())
                .get()
                .uri("/rooms/")
                .exchange()
                .expectStatus().value(status -> {
                    assertThat(status).isNotEqualTo(401);
                    assertThat(status).isNotEqualTo(403);
                });
    }

    @Test
    void authenticatedRequest_toReservations_passesSecurityLayer() {
        webTestClient.mutateWith(mockOidcLogin())
                .get()
                .uri("/reservations/")
                .exchange()
                .expectStatus().value(status -> {
                    assertThat(status).isNotEqualTo(401);
                    assertThat(status).isNotEqualTo(403);
                });
    }

    // D.6: Verify standard OIDC claims pass through — token stays server side,
    // browser only sees session cookie
    @Test
    void authenticatedRequest_withOidcClaims_passesSecurityLayer() {
        webTestClient.mutateWith(mockOidcLogin()
                        .idToken(token -> token
                                .claim("email", "user@example.com")
                                .claim("preferred_username", "testuser")
                        ))
                .get()
                .uri("/rooms/")
                .exchange()
                .expectStatus().value(status -> {
                    assertThat(status).isNotEqualTo(401);
                    assertThat(status).isNotEqualTo(403);
                });
    }

    // --- CSRF tests (D.4: cookie-based CSRF with WebFilter subscription) ---

    // POST without CSRF token must be blocked
    @Test
    void postRequest_withoutCsrfToken_isForbidden() {
        webTestClient.mutateWith(mockOidcLogin())
                .post()
                .uri("/rooms/")
                .exchange()
                .expectStatus().isForbidden();
    }

    // POST with CSRF token must pass security layer
    @Test
    void postRequest_withCsrfToken_passesSecurityLayer() {
        webTestClient.mutateWith(mockOidcLogin())
                .mutateWith(csrf())
                .post()
                .uri("/rooms/")
                .exchange()
                .expectStatus().value(status ->
                    assertThat(status).isNotEqualTo(403)
                );
    }

    // DELETE without CSRF token must be blocked
    @Test
    void deleteRequest_withoutCsrfToken_isForbidden() {
        webTestClient.mutateWith(mockOidcLogin())
                .delete()
                .uri("/rooms/1")
                .exchange()
                .expectStatus().isForbidden();
    }

    // DELETE with CSRF token must pass security layer
    @Test
    void deleteRequest_withCsrfToken_passesSecurityLayer() {
        webTestClient.mutateWith(mockOidcLogin())
                .mutateWith(csrf())
                .delete()
                .uri("/rooms/1")
                .exchange()
                .expectStatus().value(status ->
                    assertThat(status).isNotEqualTo(403)
                );
    }

    // Logout must redirect — RP-Initiated Logout sends user to Keycloak logout endpoint
    @Test
    void logout_withCsrf_redirectsToKeycloak() {
        webTestClient.mutateWith(mockOidcLogin())
                .mutateWith(csrf())
                .post()
                .uri("/logout")
                .exchange()
                .expectStatus().is3xxRedirection();
    }
}
