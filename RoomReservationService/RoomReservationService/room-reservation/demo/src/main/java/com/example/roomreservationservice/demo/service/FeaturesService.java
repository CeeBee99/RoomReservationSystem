package com.example.roomreservationservice.demo.service;

import com.example.roomreservationservice.demo.model.Features;
import com.example.roomreservationservice.demo.data.FeaturesRepository;
import org.springframework.stereotype.Service;

@Service
public class FeaturesService {

    private final FeaturesRepository featuresRepository;

    public FeaturesService(FeaturesRepository featuresRepository) {
        this.featuresRepository = featuresRepository;
    }

    public Features save(Features features) {
        return featuresRepository.save(features);
    }
}