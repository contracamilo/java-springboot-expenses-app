package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // 400 Bad Request
public class CategoryFixedException extends RuntimeException {
    public CategoryFixedException(String message) {
        super(message);
    }
} 