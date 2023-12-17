package com.example.financialtracker.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "SELECT * FROM category WHERE user_id = :userId AND status = true", nativeQuery = true)
    List<Category> findCategoryByUserId(long userId);

    @Query(value = "SELECT * FROM category WHERE category_id = :categoryId AND user_id = :userId LIMIT 1", nativeQuery = true)
    Optional<Category> findCategoryByIdAndUserId(long categoryId, long userId);

    @Query(value = "SELECT * FROM category WHERE title = :title AND user_id = :userId LIMIT 1", nativeQuery = true)
    Optional<Category> findCategoryByTitleAndUserId(String title, long userId);

    @Query(value = "SELECT * FROM category WHERE category_id <> :categoryId AND title = :title AND user_id = :userId LIMIT 1", nativeQuery = true)
    Optional<Category> findByTitleAndUserIdNotId(String title, long userId, long categoryId);
 }
