package com.example.roomreservationservice.demo.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.roomreservationservice.demo.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
