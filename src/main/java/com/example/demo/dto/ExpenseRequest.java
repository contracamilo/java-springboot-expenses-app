package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseRequest {
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private String categoryName;
    private Long userId;

    // Getters y setters
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
} 