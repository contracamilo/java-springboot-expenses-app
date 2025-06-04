package com.example.demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseRequestDto {
    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor que cero")
    private BigDecimal amount;

    @NotNull(message = "La fecha es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate date;

    private String description;

    @NotNull(message = "El ID de la categoría es obligatorio")
    private Long categoryId;

    // Getters y Setters
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
} 