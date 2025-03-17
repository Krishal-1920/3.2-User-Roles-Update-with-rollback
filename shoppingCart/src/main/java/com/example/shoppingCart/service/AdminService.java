package com.example.shoppingCart.service;

import com.example.shoppingCart.entity.User;
import com.example.shoppingCart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    // Fetching All the users By Admin
    public List<User> getUsers(String email, String phoneNumber) {
        return userRepository.findByEmailOrPhoneNumber(email, phoneNumber);
    }
}
