package com.example.financialtracker.expense;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query(value = "SELECT * FROM expense WHERE user_id = :userId", nativeQuery = true)
    List<Expense> findExpensesByUser(long userId);

    @Query(value = "SELECT * FROM expense WHERE user_id = :userId AND expense_id = :expenseId LIMIT 1", nativeQuery = true)
    Optional<Expense> findSingleExpense(long userId, long expenseId);
}
