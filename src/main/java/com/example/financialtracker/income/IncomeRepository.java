package com.example.financialtracker.income;

import com.example.financialtracker.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IncomeRepository extends JpaRepository<Income, Long> {
    @Query(value = "SELECT * FROM income WHERE user_id = :userId", nativeQuery = true)
    List<Income> findIncomesByUser(long userId);

    @Query(value = "SELECT * FROM income WHERE user_id = :userId AND income_id = :incomeId LIMIT 1", nativeQuery = true)
    Optional<Income> findSingleIncome(long userId, long incomeId);

    @Query(value = "SELECT * FROM income WHERE user_id = :userId AND category_id = :categoryId", nativeQuery = true)
    List<Income> findIncomesByCategory(long userId, long categoryId);

    @Query(value = "SELECT YEAR(e.createdOn) as year, MONTH(e.createdOn) as month, e.incomeCategory.title as category, SUM(e.amount) as total " +
            "FROM Income e " +
            "WHERE e.user = :user " +
            "GROUP BY YEAR(e.createdOn), MONTH(e.createdOn), e.incomeCategory.title ")
    List<Map<String, Object>> findIncomePerMonthPerCat(User user);

}
