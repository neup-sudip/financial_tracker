package com.example.financialtracker.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final JdbcTemplate jdbcTemplate;

    private final int limit = 12;

    public List<TransactionDto> getTransactions(long userId) {
        String sql = "SELECT 'income' AS record_type, title, description, amount, created_on FROM income WHERE user_id = ?" +
                " UNION ALL " +
                "SELECT 'expense' AS record_type, title, description, amount, created_on FROM expense WHERE user_id = ?" +
                " ORDER BY created_on DESC";

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(TransactionDto.class),
                userId, userId);
    }

    public List<TransactionDto> getDataByUserIdAndSearchTerm(long userId, String searchTerm, int page) {

        int offset = 0;
        if (page > 1) {
            offset = (page - 1) * limit;
        }

        String sql = "SELECT 'income' AS record_type, title, description, amount, created_on FROM income WHERE user_id = ? AND (LOWER(title) LIKE LOWER(?) OR LOWER(description) LIKE LOWER(?))" +
                " UNION ALL " +
                "SELECT 'expense' AS record_type, title, description, amount, created_on FROM expense WHERE user_id = ? AND (LOWER(title) LIKE LOWER(?) OR LOWER(description) LIKE LOWER(?))" +
                " ORDER BY created_on DESC LIMIT ? OFFSET ?";

        searchTerm = "%" + searchTerm.toLowerCase() + "%";

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(TransactionDto.class),
                userId, searchTerm, searchTerm, userId, searchTerm, searchTerm, limit, offset);
    }
}
