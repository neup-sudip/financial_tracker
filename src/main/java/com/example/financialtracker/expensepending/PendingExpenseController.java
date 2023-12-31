package com.example.financialtracker.expensepending;

import com.example.financialtracker.exception.CustomException;
import com.example.financialtracker.expense.ExpenseReqDto;
import com.example.financialtracker.expense.ExpenseResDto;
import com.example.financialtracker.user.User;
import com.example.financialtracker.wrapper.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/pending")
public class PendingExpenseController {

    private final PendingExpenseService pendingExpenseService;
    private final HttpServletRequest request;

    static final String SERVER_ERROR_MESSAGE = "Internal Server Error !";
    static final int SERVER_ERROR_CODE = 500;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PendingExpense>>> getAllPendingList() {
        User user = (User) request.getAttribute("user");
        List<PendingExpense> pendingExpenses = pendingExpenseService.getAllPendingExpense(user.getUserId());
        return ResponseEntity.status(200).body(new ApiResponse<>(true, pendingExpenses, "Pending Expenses fetched !"));
    }

    @PostMapping("/accept/{id}")
    public ResponseEntity<ApiResponse<ExpenseResDto>> acceptPending(@RequestBody ExpenseReqDto expenseReqDto, @PathVariable long id) {
        User user = (User) request.getAttribute("user");
        try {
            ExpenseResDto expenseResDto = pendingExpenseService.handleAccept(id, user, expenseReqDto);
            return ResponseEntity.status(200).body(new ApiResponse<>(true, expenseResDto, "Expense accepted !"));
        } catch (CustomException exception) {
            throw new CustomException(exception.getMessage(), exception.getStatus());
        } catch (Exception exception) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> decline(@PathVariable long id) {
        User user = (User) request.getAttribute("user");
        pendingExpenseService.declinePendingExpense(user, id);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, null, "Expense declined !"));
    }
}
