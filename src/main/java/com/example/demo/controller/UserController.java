package com.example.demo.controller;

import com.example.demo.dto.UpdateUserRequestDto;
import com.example.demo.dto.UserDto;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("isAuthenticated()")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        User currentUserEntity = userService.getCurrentAuthenticatedUser();
        // Convert User entity to UserDto
        UserDto userDto = new UserDto(currentUserEntity.getId(), currentUserEntity.getName(), currentUserEntity.getEmail());
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/me")
    public ResponseEntity<UserDto> updateCurrentUser(@Valid @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        User currentUserEntity = userService.getCurrentAuthenticatedUser();
        // Prepare UserDto for update, ensuring ID is correctly sourced
        UserDto userUpdateRequest = new UserDto(currentUserEntity.getId(), updateUserRequestDto.getName(), updateUserRequestDto.getEmail());
        UserDto updatedUser = userService.updateUser(currentUserEntity.getId(), userUpdateRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteCurrentUser() {
        User currentUserEntity = userService.getCurrentAuthenticatedUser();
        userService.deleteUser(currentUserEntity.getId());
        return ResponseEntity.noContent().build();
    }
} 