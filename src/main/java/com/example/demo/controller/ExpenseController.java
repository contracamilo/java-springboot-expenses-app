package com.example.demo.controller;

import com.example.demo.dto.ExpenseRequestDto;
import com.example.demo.dto.ExpenseResponseDto;
import com.example.demo.repository.MonthlyExpenseReport;
import com.example.demo.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@PreAuthorize("isAuthenticated()")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseResponseDto> createExpense(@Valid @RequestBody ExpenseRequestDto expenseRequestDto) {
        ExpenseResponseDto createdExpense = expenseService.createExpenseForCurrentUser(expenseRequestDto);
        return new ResponseEntity<>(createdExpense, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponseDto>> getAllExpenses() {
        List<ExpenseResponseDto> expenses = expenseService.getAllExpensesForCurrentUser();
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDto> getExpenseById(@PathVariable Long id) {
        ExpenseResponseDto expenseDto = expenseService.getExpenseByIdForCurrentUser(id);
        return ResponseEntity.ok(expenseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDto> updateExpense(@PathVariable Long id, @Valid @RequestBody ExpenseRequestDto expenseRequestDto) {
        ExpenseResponseDto updatedExpense = expenseService.updateExpenseForCurrentUser(id, expenseRequestDto);
        return ResponseEntity.ok(updatedExpense);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpenseForCurrentUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ExpenseResponseDto>> getExpensesByCategory(@PathVariable Long categoryId) {
        List<ExpenseResponseDto> expenses = expenseService.getExpensesByCategoryForCurrentUser(categoryId);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<ExpenseResponseDto>> getExpensesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<ExpenseResponseDto> expenses = expenseService.getExpensesByDateRangeForCurrentUser(startDate, endDate);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/reports/monthly")
    public ResponseEntity<List<MonthlyExpenseReport>> getMonthlyReport(
            @RequestParam int year,
            @RequestParam int month) {
        List<MonthlyExpenseReport> report = expenseService.getMonthlyReportForCurrentUser(year, month);
        return ResponseEntity.ok(report);
    }
} 