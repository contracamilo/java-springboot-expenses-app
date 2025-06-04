package com.example.demo.service;

import com.example.demo.dto.AuthResponseDto;
import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.RegisterRequestDto;
import com.example.demo.dto.UserDto;
import com.example.demo.model.User;
import java.util.Optional;

public interface UserService {
    UserDto registerUser(RegisterRequestDto registerRequestDto);
    AuthResponseDto loginUser(LoginRequestDto loginRequestDto);
    User getCurrentAuthenticatedUser();
    UserDto getUserById(Long id);
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
} 