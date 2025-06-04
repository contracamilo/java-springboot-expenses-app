package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409 Conflict
public class CategoryNameAlreadyExistsException extends RuntimeException {
    public CategoryNameAlreadyExistsException(String message) {
        super(message);
    }
} 