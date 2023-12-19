package com.example.financialtracker.expense;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query(value = "SELECT * FROM expense WHERE user_id = :userId", nativeQuery = true)
    List<Expense> findExpensesByUser(long userId);

    @Query(value = "SELECT * FROM expense WHERE user_id = :userId AND expense_id = :expenseId LIMIT 1", nativeQuery = true)
    Optional<Expense> findSingleExpense(long userId, long expenseId);

    @Query(value = "SELECT * FROM expense WHERE user_id = :userId AND category_id = :categoryId", nativeQuery = true)
    List<Expense> findExpensesByCategory(long userId, long categoryId);

    @Query(nativeQuery = true,
            value = "SELECT EXTRACT(YEAR FROM created_on) as year, EXTRACT(MONTH FROM created_on) as month, "
                    + "category_id as categoryId, SUM(amount) as total "
                    + "FROM expense "
                    + "WHERE user_id = :userId "
                    + "GROUP BY year, month, categoryId")
    List<Map<String, Object>> findIncomePerMonthPerCat(long userId);
}
