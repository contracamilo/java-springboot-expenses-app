package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoryDto {
    private Long id;

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    private String name;

    private String description;
    private boolean fixed;

    public CategoryDto() {}

    public CategoryDto(Long id, String name, String description, boolean fixed) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.fixed = fixed;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isFixed() { return fixed; }
    public void setFixed(boolean fixed) { this.fixed = fixed; }
} 