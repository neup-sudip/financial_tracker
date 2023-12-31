package com.example.financialtracker.expensepending;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PendingExpenseRepository extends JpaRepository<PendingExpense, Long> {
    @Query(value = "SELECT * FROM pending_expense WHERE user_id = :userId", nativeQuery = true)
    List<PendingExpense> getUserPendingExpense(long userId);

    @Modifying
    @Query(value = "DELETE FROM pending_expense WHERE user_id = :userId AND pending_id = :pendingId", nativeQuery = true)
    void deletePendingExpense(long userId, long pendingId);

    @Query(value = "SELECT * FROM pending_expense WHERE user_id = :userId AND pending_id = :pendingId", nativeQuery = true)
    Optional<PendingExpense> getPendingExpense(long userId, long pendingId);
}
