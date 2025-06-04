package com.example.demo.repository;

import java.math.BigDecimal;

public interface MonthlyExpenseReport {
    String getCategoryName();
    BigDecimal getTotalAmount();
} 