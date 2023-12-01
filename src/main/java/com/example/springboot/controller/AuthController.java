package com.example.springboot.controller;

import com.example.springboot.exception.BadRequestException;
import com.example.springboot.model.Customer;
import com.example.springboot.payload.ApiResponse;
import com.example.springboot.payload.AuthResponse;
import com.example.springboot.payload.LoginRequest;
import com.example.springboot.payload.SignUpRequest;
import com.example.springboot.repository.CustomerRepository;
import com.example.springboot.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/welcome")
public class AuthController {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenProvider tokenProvider;
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest){
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest signUpRequest){
        if (customerRepository.existsByEmail(signUpRequest.getEmail())){
            throw new BadRequestException("Email address already in use.");
        }
        Customer customer= new Customer();
        customer.setName(signUpRequest.getName());
        customer.setEmail(signUpRequest.getEmail());
        customer.setPassword(signUpRequest.getPassword());
        customer.setContacts(signUpRequest.getContacts());
        customer.setHobbies(signUpRequest.getHobbies());
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        Customer result = customerRepository.save(customer);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(location)
                .body(new ApiResponse(true,"User registered successfully"));
    }
}
