package com.example.roomreservationservice.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.roomreservationservice.demo.model.User;
import com.example.roomreservationservice.demo.data.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User newUser) {
        return userRepository.save(newUser);
    }
}
