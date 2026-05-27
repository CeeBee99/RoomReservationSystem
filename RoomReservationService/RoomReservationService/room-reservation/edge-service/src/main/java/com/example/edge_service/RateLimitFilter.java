package com.example.edge_service;

import org.springframework.core.io.buffer.DataBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.reactivestreams.Publisher;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class RateLimitFilter implements GlobalFilter{
    private final RedisTemplate<String, String>  redisTemplate;

    public RateLimitFilter(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain){
        String ip = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        String key = "rate:" + ip;
        Long count = redisTemplate.opsForValue().increment(key);
        if(count != null && count == 1){
            redisTemplate.expire(key, Duration.ofMinutes(1));
        }
        if(count != null && count > 100){
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            byte[] bytes = "Too Many Requests".getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer =  exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse().writeWith(Mono.just(buffer));
            
        }
         return chain.filter(exchange);
    }

}
