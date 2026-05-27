package com.example.edge_service.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.ServerCsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(
            ServerHttpSecurity http,
            ObjectProvider<ReactiveClientRegistrationRepository> clientRegistrationRepositoryProvider) {

        ReactiveClientRegistrationRepository clientRegistrationRepository =
            clientRegistrationRepositoryProvider.getIfAvailable();
        boolean keycloakEnabled = clientRegistrationRepository != null;

        http
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(
                    new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED))
            )
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(new ServerCsrfTokenRequestAttributeHandler())
            )
            .requestCache(Customizer.withDefaults());

        if (keycloakEnabled) {
            OidcClientInitiatedServerLogoutSuccessHandler logoutHandler =
                new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository);
            logoutHandler.setPostLogoutRedirectUri("{baseUrl}");

            http
                .authorizeExchange(exchanges -> exchanges
                    .pathMatchers("/actuator/health", "/actuator/info").permitAll()
                    .anyExchange().authenticated()
                )
                .oauth2Login(Customizer.withDefaults())
                .logout(logout -> logout.logoutSuccessHandler(logoutHandler));
        } else {
            http.authorizeExchange(exchanges -> exchanges
                .pathMatchers("/actuator/health", "/actuator/info").permitAll()
                .anyExchange().permitAll()
            );
        }

        return http.build();
    }
}
