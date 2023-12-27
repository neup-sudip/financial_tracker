package com.example.financialtracker.expense;

import com.example.financialtracker.user.User;
import com.example.financialtracker.wrapper.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/expense")
public class ExpenseController {
    
    private final ExpenseService expenseService;

    private final HttpServletRequest request;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<ExpenseResDto>>> getAllExpense(@RequestParam(name = "category", defaultValue = "0") long catId ){
        User user = (User) request.getAttribute("user");
        List<ExpenseResDto> expenseResDtos = expenseService.getAllUserExpenses(user.getUserId(), catId);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, expenseResDtos, "Expenses fetched !"));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<ApiResponse<List<ExpenseResDto>>> getExpensesByCategory(@PathVariable long id){
        User user = (User) request.getAttribute("user");
        List<ExpenseResDto> expenseResDtos = expenseService.getExpensesByCategory(user.getUserId(), id);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, expenseResDtos, "Category expenses fetched !"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseResDto>> getSingleExpense(@PathVariable long id){
        User user = (User) request.getAttribute("user");
        ExpenseResDto expenseResDto = expenseService.getSingleExpense(user.getUserId(), id);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, expenseResDto, "Expense fetched !"));
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<ExpenseResDto>> createExpense(@Valid @RequestBody ExpenseReqDto expenseReqDto){
        User user = (User) request.getAttribute("user");
        boolean hasConfirm = (boolean) request.getAttribute("confirm");
        ExpenseResDto expenseResDto = expenseService.createExpense(expenseReqDto, user.getUserId(), hasConfirm);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, expenseResDto, "Expense created !"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseResDto>> updateExpense(@Valid @RequestBody ExpenseReqDto expenseReqDto, @PathVariable long id){
        User user = (User) request.getAttribute("user");
        boolean hasConfirm = (boolean) request.getAttribute("confirm");
        ExpenseResDto expenseResDto = expenseService.updateExpense(expenseReqDto, user.getUserId(), id, hasConfirm);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, expenseResDto, "Expense updated !"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> removeExpense(@PathVariable long id){
        User user = (User) request.getAttribute("user");
        expenseService.removeExpense(user.getUserId(), id);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, "", "Expense deleted !"));
    }
}
