package com.example.financialtracker.transaction;

import com.example.financialtracker.user.User;
import com.example.financialtracker.wrapper.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    private final HttpServletRequest request;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<TransactionDto>>> getTransactions(@RequestParam(name = "query" , defaultValue = "") String query, @RequestParam(name = "page", defaultValue = "1") int page) {
        User user = (User) request.getAttribute("user");

        List<TransactionDto> transactions =  transactionService.getDataByUserIdAndSearchTerm(user.getUserId(), query, page);
        ApiResponse<List<TransactionDto>> response = new ApiResponse<>(true, transactions, "Transactions fetched !");
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/download")
    public ResponseEntity<ApiResponse<List<TransactionDto>>> downloadTransaction() {
        User user = (User) request.getAttribute("user");

        List<TransactionDto> transactions =  transactionService.getTransactions(user.getUserId());
        ApiResponse<List<TransactionDto>> response = new ApiResponse<>(true, transactions, "Transactions download started !");
        return ResponseEntity.status(200).body(response);
    }
}
