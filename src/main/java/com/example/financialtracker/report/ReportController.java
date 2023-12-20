package com.example.financialtracker.report;

import com.example.financialtracker.expense.ExpenseService;
import com.example.financialtracker.income.IncomeService;
import com.example.financialtracker.user.User;
import com.example.financialtracker.wrapper.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        List<PerYearMonthCat> report = incomeService.getPerMonthReport(user);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, report, "Income Report fetched !"));
    }

    @GetMapping("/expense/per-ymc")
    public ResponseEntity<ApiResponse<List<PerYearMonthCat>>> perYearMonthCatExpense(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        List<PerYearMonthCat> report = expenseService.getPerMonthReport(user);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, report, "Expense Report fetched !"));
    }

    @GetMapping("/expense/per-ymc/{id}")
    public ResponseEntity<ApiResponse<List<PerMonthCatExpense>>> perMonthCatExpense( @PathVariable long id, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        List<PerMonthCatExpense> report = expenseService.getPerMonthCatExpense(user.getUserId(), id);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, report, "Expense per category report fetched !"));
    }
}
