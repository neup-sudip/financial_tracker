package com.example.financialtracker.cron;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CronRepository extends JpaRepository<Cron, Long> {
    @Query(value = "SELECT cron_date FROM cron", nativeQuery = true)
    List<String> getAllCronDate();
}
