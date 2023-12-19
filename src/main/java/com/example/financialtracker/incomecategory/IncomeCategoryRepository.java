package com.example.financialtracker.incomecategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IncomeCategoryRepository extends JpaRepository<IncomeCategory, Long> {
    @Query(value = "SELECT * FROM income_category WHERE user_id = :userId", nativeQuery = true)
    List<IncomeCategory> findAllCategory(long userId);

    @Query(value = "SELECT * FROM income_category WHERE user_id = :userId AND status = true", nativeQuery = true)
    List<IncomeCategory> findAllActiveCategory(long userId);

    @Query(value = "SELECT * FROM income_category WHERE category_id = :categoryId AND user_id = :userId LIMIT 1", nativeQuery = true)
    Optional<IncomeCategory> findSingleCategory(long categoryId, long userId);

    @Query(value = "SELECT * FROM income_category WHERE title = :title AND user_id = :userId LIMIT 1", nativeQuery = true)
    Optional<IncomeCategory> findCategoryByTitle(String title, long userId);

    @Query(value = "SELECT * FROM income_category WHERE category_id <> :categoryId AND title = :title AND user_id = :userId LIMIT 1", nativeQuery = true)
    Optional<IncomeCategory> findByTitleAndNotId(String title, long userId, long categoryId);
}
