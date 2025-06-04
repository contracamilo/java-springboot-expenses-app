package com.example.demo.service;

import com.example.demo.dto.ExpenseRequestDto;
import com.example.demo.dto.ExpenseResponseDto;
import com.example.demo.repository.MonthlyExpenseReport; // Importar la interfaz del reporte

import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {
    List<ExpenseResponseDto> getAllExpensesForCurrentUser();
    ExpenseResponseDto getExpenseByIdForCurrentUser(Long id);
    ExpenseResponseDto createExpenseForCurrentUser(ExpenseRequestDto expenseRequestDto);
    ExpenseResponseDto updateExpenseForCurrentUser(Long id, ExpenseRequestDto expenseRequestDto);
    void deleteExpenseForCurrentUser(Long id);

    // Métodos de filtrado
    List<ExpenseResponseDto> getExpensesByCategoryForCurrentUser(Long categoryId);
    List<ExpenseResponseDto> getExpensesByDateRangeForCurrentUser(LocalDate startDate, LocalDate endDate);
    List<MonthlyExpenseReport> getMonthlyReportForCurrentUser(int year, int month);
} 