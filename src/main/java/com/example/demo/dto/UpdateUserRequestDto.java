package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateUserRequestDto {

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 and 50 caracteres.")
    private String name;

    @NotBlank(message = "El email no puede estar vacío.")
    @Email(message = "El formato del email no es válido.")
    @Size(max = 100, message = "El email no puede exceder los 100 caracteres.")
    private String email;

    public UpdateUserRequestDto() {
    }

    public UpdateUserRequestDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
} 