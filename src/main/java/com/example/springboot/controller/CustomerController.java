package com.example.springboot.controller;

import com.example.springboot.exception.ResourceNotFoundException;
import com.example.springboot.model.Customer;
import com.example.springboot.repository.CustomerRepository;
import com.example.springboot.security.CurrentUser;
import com.example.springboot.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public Customer getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return customerRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }
}
