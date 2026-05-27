package com.example.roomreservationservice.demo.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.roomreservationservice.demo.model.Features;

@Repository
public interface FeaturesRepository extends JpaRepository<Features, Long> {

    List<Features> findByRoom_Id(Long roomId);

    void deleteByRoom_Id(Long roomId);
}