package com.example.financialtracker.expensepending;

import com.example.financialtracker.exception.CustomException;
import com.example.financialtracker.expense.Expense;
import com.example.financialtracker.expense.ExpenseRepository;
import com.example.financialtracker.expense.ExpenseReqDto;
import com.example.financialtracker.expense.ExpenseResDto;
import com.example.financialtracker.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PendingExpenseService {
    private final PendingExpenseRepository pendingExpenseRepository;
    private final ExpenseRepository expenseRepository;

    static final String SERVER_ERROR_MESSAGE = "Internal Server Error !";
    static final int SERVER_ERROR_CODE = 500;

    public List<PendingExpense> getAllPendingExpense(long userId) {
        try {
            return pendingExpenseRepository.getUserPendingExpense(userId);
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }

    public void createPendingExpense(PendingExpense pendingExpense) {
        try {
            pendingExpenseRepository.save(pendingExpense);
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }

    @Transactional
    public ExpenseResDto handleAccept(long pendingId, User user, ExpenseReqDto expenseReqDto) {
        Optional<PendingExpense> pendingExpense = pendingExpenseRepository.getPendingExpense(user.getUserId(), pendingId);
        if (pendingExpense.isEmpty()) {
            throw new CustomException("Can not get pending expense at the moment !", 400);
        }
        Expense expense = new Expense(expenseReqDto, user);
        Expense savedExpanse = expenseRepository.save(expense);
        pendingExpenseRepository.deletePendingExpense(user.getUserId(), pendingId);
        return new ExpenseResDto(savedExpanse);
    }

    public void declinePendingExpense(User user, long pendingId) {
        try {
            pendingExpenseRepository.deletePendingExpense(user.getUserId(), pendingId);
        } catch (Exception exception) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }

    public PendingExpense getPendingExpense(long userId, long pendingId) {
        Optional<PendingExpense> pendingExpense = pendingExpenseRepository.getPendingExpense(userId, pendingId);
        if (pendingExpense.isEmpty()) {
            throw new CustomException("Can not get pending expense at the moment !", 400);
        } else {
            return pendingExpense.get();
        }
    }
}
