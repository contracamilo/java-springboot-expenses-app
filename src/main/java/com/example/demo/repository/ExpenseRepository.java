package com.example.demo.repository;

import com.example.demo.model.Expense;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserOrderByDateDesc(User user);
    Optional<Expense> findByIdAndUser(Long id, User user);
    List<Expense> findByUserAndCategoryIdOrderByDateDesc(User user, Long categoryId);
    List<Expense> findByUserAndDateBetweenOrderByDateDesc(User user, LocalDate startDate, LocalDate endDate);

    // Para el reporte mensual (opcional)
    @Query("SELECT e.category.name as categoryName, SUM(e.amount) as totalAmount " +
           "FROM Expense e " +
           "WHERE e.user = :user AND YEAR(e.date) = :year AND MONTH(e.date) = :month " +
           "GROUP BY e.category.name")
    List<MonthlyExpenseReport> getMonthlyReportByUser(@Param("user") User user, @Param("year") int year, @Param("month") int month);
} 