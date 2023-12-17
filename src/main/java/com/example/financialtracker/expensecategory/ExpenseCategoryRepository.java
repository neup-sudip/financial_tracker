package com.example.financialtracker.expensecategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long> {
    @Query(value = "SELECT * FROM expense_category WHERE user_id = :userId AND status = true", nativeQuery = true)
    List<ExpenseCategory> findCategoryByUserId(long userId);

    @Query(value = "SELECT * FROM expense_category WHERE category_id = :categoryId AND user_id = :userId LIMIT 1", nativeQuery = true)
    Optional<ExpenseCategory> findCategoryByIdAndUserId(long categoryId, long userId);

    @Query(value = "SELECT * FROM expense_category WHERE title = :title AND user_id = :userId LIMIT 1", nativeQuery = true)
    Optional<ExpenseCategory> findCategoryByTitleAndUserId(String title, long userId);

    @Query(value = "SELECT * FROM expense_category WHERE category_id <> :categoryId AND title = :title AND user_id = :userId LIMIT 1", nativeQuery = true)
    Optional<ExpenseCategory> findByTitleAndUserIdNotId(String title, long userId, long categoryId);
 }
