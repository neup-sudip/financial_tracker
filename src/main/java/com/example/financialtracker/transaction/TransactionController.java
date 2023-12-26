package com.example.financialtracker.transaction;

import com.example.financialtracker.user.User;
import com.example.financialtracker.wrapper.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<TransactionDto>>> getTransactions(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");

        List<TransactionDto> transactions =  transactionService.getAllTransactions(user.getUserId());
        ApiResponse<List<TransactionDto>> response = new ApiResponse<>(true, transactions, "Transactions fetched !");
        return ResponseEntity.status(200).body(response);
    }
}
