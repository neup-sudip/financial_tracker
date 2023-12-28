package com.example.financialtracker.transaction;

import com.example.financialtracker.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final JdbcTemplate jdbcTemplate;

    static final int LIMIT = 12;
    static final String SERVER_ERROR_MESSAGE = "Internal Server Error !";
    static final int SERVER_ERROR_CODE = 500;

    public List<TransactionDto> getTransactions(long userId) {
        String sql = "SELECT 'income' AS record_type, title, description, amount, created_on FROM income WHERE user_id = ?" +
                " UNION ALL " +
                "SELECT 'expense' AS record_type, title, description, amount, created_on FROM expense WHERE user_id = ?" +
                " ORDER BY created_on DESC";

        try {
            return jdbcTemplate.query(
                    sql,
                    new BeanPropertyRowMapper<>(TransactionDto.class),
                    userId, userId);
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }

    public List<TransactionDto> getDataByUserIdAndSearchTerm(long userId, String searchTerm, int page) {

        int offset = 0;
        if (page > 1) {
            offset = (page - 1) * LIMIT;
        }

        String sql = "SELECT 'income' AS record_type, title, description, amount, created_on FROM income WHERE user_id = ? AND (LOWER(title) LIKE LOWER(?) OR LOWER(description) LIKE LOWER(?))" +
                " UNION ALL " +
                "SELECT 'expense' AS record_type, title, description, amount, created_on FROM expense WHERE user_id = ? AND (LOWER(title) LIKE LOWER(?) OR LOWER(description) LIKE LOWER(?))" +
                " ORDER BY created_on DESC LIMIT ? OFFSET ?";

        searchTerm = "%" + searchTerm.toLowerCase() + "%";
        try {
            return jdbcTemplate.query(
                    sql,
                    new BeanPropertyRowMapper<>(TransactionDto.class),
                    userId, searchTerm, searchTerm, userId, searchTerm, searchTerm, LIMIT, offset);
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }
}
