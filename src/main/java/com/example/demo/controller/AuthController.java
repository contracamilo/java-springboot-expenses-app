package com.example.demo.controller;

import com.example.demo.dto.AuthResponseDto;
import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.RegisterRequestDto;
import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        UserDto registeredUser = userService.registerUser(registerRequestDto);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> loginUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        AuthResponseDto authResponse = userService.loginUser(loginRequestDto);
        return ResponseEntity.ok(authResponse);
    }
} 