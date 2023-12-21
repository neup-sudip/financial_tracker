package com.example.financialtracker.expense;

import com.example.financialtracker.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query(value = "SELECT * FROM expense WHERE user_id = :userId", nativeQuery = true)
    List<Expense> findExpensesByUser(long userId);

    @Query(value = "SELECT * FROM expense WHERE user_id = :userId AND category_id = :categoryId", nativeQuery = true)
    List<Expense> findByUserAndCategory(long userId, long categoryId);

    @Query(value = "SELECT * FROM expense WHERE user_id = :userId AND expense_id = :expenseId LIMIT 1", nativeQuery = true)
    Optional<Expense> findSingleExpense(long userId, long expenseId);

    @Query(value = "SELECT * FROM expense WHERE user_id = :userId AND category_id = :categoryId", nativeQuery = true)
    List<Expense> findExpensesByCategory(long userId, long categoryId);

    @Query(value = "SELECT YEAR(e.createdOn) as year, MONTH(e.createdOn) as month, e.expenseCategory.title as category, SUM(e.amount) as total " +
            "FROM Expense e " +
            "WHERE e.user = :user " +
            "GROUP BY YEAR(e.createdOn), MONTH(e.createdOn), e.expenseCategory.title ")
    List<Map<String, Object>> findExpensePerMonthPerCat(User user);

    @Query(nativeQuery = true,
            value = "SELECT EXTRACT(YEAR FROM created_on) as year, EXTRACT(MONTH FROM created_on) as month, "
                    + "SUM(amount) as total, COUNT(*) as count "
                    + "FROM expense "
                    + "WHERE user_id = :userId "
                    + "AND category_id = :categoryId "
                    + "GROUP BY year, month")
    List<Map<String, Object>> findPerMonthCatExpense(long userId, long categoryId);
}
