package com.example.edge_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class EdgeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EdgeServiceApplication.class, args);
    }


    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.justOrEmpty(
            exchange.getRequest().getRemoteAddress()
        ).map(addr -> addr.getAddress().getHostAddress())
         .defaultIfEmpty("unknown");
    }
}

