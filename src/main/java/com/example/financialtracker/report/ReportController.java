package com.example.financialtracker.report;

import com.example.financialtracker.expense.ExpenseService;
import com.example.financialtracker.income.IncomeService;
import com.example.financialtracker.user.User;
import com.example.financialtracker.wrapper.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/report")
public class ReportController {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    @GetMapping("/income/per-ymc")
    public ResponseEntity<ApiResponse<List<PerYearMonthCat>>> perYearMonthCatIncome(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        List<PerYearMonthCat> report = incomeService.getPerMonthReport(user.getUserId());
        return ResponseEntity.status(200).body(new ApiResponse<>(true, report, "Report fetched !"));
    }

    @GetMapping("/expense/per-ymc")
    public ResponseEntity<ApiResponse<List<PerYearMonthCat>>> perYearMonthCatExpense(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        List<PerYearMonthCat> report = expenseService.getPerMonthReport(user.getUserId());
        return ResponseEntity.status(200).body(new ApiResponse<>(true, report, "Report fetched !"));
    }
}
