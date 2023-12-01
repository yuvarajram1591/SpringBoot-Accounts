package com.example.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/welcome")
public class WelcomeUser {
    @GetMapping("/helloUser")
    public String helloUser(){
        return "Welcome User Login Page";
    }
}
