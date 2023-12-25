package com.example.financialtracker.years;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface YearsRepository extends JpaRepository<Years, Long> {
    @Query(value = "SELECT * FROM Years WHERE category_id =:categoryId", nativeQuery = true)
    List<Years> findByCategory(long categoryId);

    @Query(value = "SELECT * FROM Years WHERE category_id =:categoryId and year =:year LIMIT 1", nativeQuery = true)
    Optional<Years> getYearByCategoryAndYear(long categoryId, int year);

    @Query(value = "SELECT * FROM years WHERE category_id = :categoryId AND year = :year LIMIT 1", nativeQuery = true)
    Optional<Years> findByCategoryAndYear(long categoryId, int year);

    @Query(value = "SELECT * FROM years WHERE category_id = :categoryId AND year = :year AND id <> :yearId LIMIT 1", nativeQuery = true)
    Optional<Years> findByCategoryAndYearNotId(long categoryId, int year, long yearId);

}

