package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseResponseDto {
    private Long id;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private CategoryDto category; // Categoría anidada

    public ExpenseResponseDto(Long id, BigDecimal amount, LocalDate date, String description, CategoryDto category) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.category = category;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public CategoryDto getCategory() { return category; }
    public void setCategory(CategoryDto category) { this.category = category; }
} 