package com.example.overlap_engine.controller;

import com.example.overlap_engine.Service.OverlapEngine;
import com.example.overlap_engine.model.OverlapCheckRequest;
import com.example.overlap_engine.model.OverlapCheckResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/overlap")
public class OverlapController {
    private final OverlapEngine overlapEngine;

    public OverlapController(OverlapEngine overlapEngine) {
        this.overlapEngine = overlapEngine;
    }

    @PostMapping("/check")
    public OverlapCheckResponse check(@Valid @RequestBody OverlapCheckRequest request) {
        return overlapEngine.checkOverlap(request);
    }
}
