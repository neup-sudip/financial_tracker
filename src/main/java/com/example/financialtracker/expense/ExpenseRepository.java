package com.example.financialtracker.expense;

import com.example.financialtracker.expensecategory.ExpenseCategory;
import com.example.financialtracker.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query(value = "SELECT e FROM Expense e WHERE (e.user = :user) AND (LOWER(e.title) LIKE LOWER(:query) OR LOWER(e.description) LIKE LOWER(:query) OR LOWER(e.expenseCategory.title) LIKE LOWER(:query))")
    List<Expense> findExpensesByUser(User user,  String query);

    @Query(value = "SELECT * FROM expense WHERE user_id = :userId AND category_id = :categoryId", nativeQuery = true)
    List<Expense> findByUserAndCategory(long userId, long categoryId);

    @Query(value = "SELECT * FROM expense WHERE user_id = :userId AND expense_id = :expenseId LIMIT 1", nativeQuery = true)
    Optional<Expense> findSingleExpense(long userId, long expenseId);

    @Query(value = "SELECT * FROM expense WHERE user_id = :userId AND category_id = :categoryId", nativeQuery = true)
    List<Expense> findExpensesByCategory(long userId, long categoryId);

    @Query(value = "SELECT COALESCE(SUM(e.amount), 0) " +
            "FROM expense e " +
            "WHERE e.user_id = :userId " +
            "  AND e.category_id = :categoryId " +
            "  AND EXTRACT(YEAR FROM e.created_on) = :year", nativeQuery = true)
    BigDecimal getTotalOfCatInYear(long userId, long categoryId, int year);

    @Query(value = "SELECT YEAR(e.createdOn) as year, MONTH(e.createdOn) as month, e.expenseCategory.title as category, SUM(e.amount) as total " +
            "FROM Expense e " +
            "WHERE e.user = :user " +
            "GROUP BY YEAR(e.createdOn), MONTH(e.createdOn), e.expenseCategory.title ")
    List<Map<String, Object>> findExpensePerMonthPerCat(User user);

    @Query(value = "SELECT YEAR(e.createdOn) as year, MONTH(e.createdOn) as month, "
                    + "SUM(e.amount) as total, COUNT(e) as count "
                    + "FROM Expense e "
                    + "WHERE e.user = :user "
                    + "AND e.expenseCategory = :category "
                    + "GROUP BY YEAR(e.createdOn), MONTH(e.createdOn)")
    List<Map<String, Object>> findPerMonthCatExpense(User user, ExpenseCategory category);
}
