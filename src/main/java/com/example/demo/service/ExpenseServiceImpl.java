package com.example.demo.service;

import com.example.demo.dto.CategoryDto;
import com.example.demo.dto.ExpenseRequestDto;
import com.example.demo.dto.ExpenseResponseDto;
import com.example.demo.exception.AccessDeniedException;
import com.example.demo.model.Category;
import com.example.demo.model.Expense;
import com.example.demo.model.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ExpenseRepository;
import com.example.demo.repository.MonthlyExpenseReport;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserService userService; // Para obtener el usuario autenticado

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponseDto> getAllExpensesForCurrentUser() {
        User currentUser = userService.getCurrentAuthenticatedUser();
        if (currentUser == null) throw new AccessDeniedException("Operación no permitida. Usuario no autenticado.");
        return expenseRepository.findByUserOrderByDateDesc(currentUser).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseResponseDto getExpenseByIdForCurrentUser(Long id) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        if (currentUser == null) throw new AccessDeniedException("Operación no permitida. Usuario no autenticado.");
        Expense expense = expenseRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new EntityNotFoundException("Gasto no encontrado con id: " + id + " para el usuario actual."));
        return convertToDto(expense);
    }

    @Override
    @Transactional
    public ExpenseResponseDto createExpenseForCurrentUser(ExpenseRequestDto expenseRequestDto) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        if (currentUser == null) throw new AccessDeniedException("Operación no permitida. Usuario no autenticado.");

        Category category = categoryRepository.findById(expenseRequestDto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con id: " + expenseRequestDto.getCategoryId()));

        Expense expense = new Expense();
        expense.setAmount(expenseRequestDto.getAmount());
        expense.setDate(expenseRequestDto.getDate());
        expense.setDescription(expenseRequestDto.getDescription());
        expense.setCategory(category);
        expense.setUser(currentUser);

        Expense savedExpense = expenseRepository.save(expense);
        return convertToDto(savedExpense);
    }

    @Override
    @Transactional
    public ExpenseResponseDto updateExpenseForCurrentUser(Long id, ExpenseRequestDto expenseRequestDto) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        if (currentUser == null) throw new AccessDeniedException("Operación no permitida. Usuario no autenticado.");

        Expense expense = expenseRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new EntityNotFoundException("Gasto no encontrado con id: " + id + " para el usuario actual."));

        Category category = categoryRepository.findById(expenseRequestDto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con id: " + expenseRequestDto.getCategoryId()));

        expense.setAmount(expenseRequestDto.getAmount());
        expense.setDate(expenseRequestDto.getDate());
        expense.setDescription(expenseRequestDto.getDescription());
        expense.setCategory(category);
        // El usuario no cambia

        Expense updatedExpense = expenseRepository.save(expense);
        return convertToDto(updatedExpense);
    }

    @Override
    @Transactional
    public void deleteExpenseForCurrentUser(Long id) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        if (currentUser == null) throw new AccessDeniedException("Operación no permitida. Usuario no autenticado.");

        Expense expense = expenseRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new EntityNotFoundException("Gasto no encontrado con id: " + id + " para el usuario actual."));
        expenseRepository.delete(expense);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponseDto> getExpensesByCategoryForCurrentUser(Long categoryId) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        if (currentUser == null) throw new AccessDeniedException("Operación no permitida. Usuario no autenticado.");
        
        categoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con id: " + categoryId));
        return expenseRepository.findByUserAndCategoryIdOrderByDateDesc(currentUser, categoryId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponseDto> getExpensesByDateRangeForCurrentUser(LocalDate startDate, LocalDate endDate) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        if (currentUser == null) throw new AccessDeniedException("Operación no permitida. Usuario no autenticado.");
        return expenseRepository.findByUserAndDateBetweenOrderByDateDesc(currentUser, startDate, endDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonthlyExpenseReport> getMonthlyReportForCurrentUser(int year, int month) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        if (currentUser == null) throw new AccessDeniedException("Operación no permitida. Usuario no autenticado.");
        return expenseRepository.getMonthlyReportByUser(currentUser, year, month);
    }

    private ExpenseResponseDto convertToDto(Expense expense) {
        CategoryDto categoryDto = new CategoryDto(
                expense.getCategory().getId(),
                expense.getCategory().getName(),
                expense.getCategory().getDescription(),
                expense.getCategory().isFixed()
        );
        return new ExpenseResponseDto(
                expense.getId(),
                expense.getAmount(),
                expense.getDate(),
                expense.getDescription(),
                categoryDto
        );
    }
} 