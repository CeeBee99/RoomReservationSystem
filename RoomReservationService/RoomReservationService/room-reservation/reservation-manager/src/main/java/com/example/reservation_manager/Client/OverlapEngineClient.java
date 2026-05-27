package com.example.reservation_manager.Client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.reservation_manager.model.OverlapCheckRequest;
import com.example.reservation_manager.model.OverlapCheckResponse;

@Component
public class OverlapEngineClient {
    private final RestTemplate rest;
    private final String url = "http://overlap-engine:8080/overlap/check";

    public OverlapEngineClient(RestTemplate rest) {
        this.rest = rest;
    }

    public OverlapCheckResponse check(OverlapCheckRequest request) {
        return rest.postForObject(url, request, OverlapCheckResponse.class);
    }
}
