package com.example.financialtracker.expense;

import com.example.financialtracker.expensecategory.ExpenseCategory;
import com.example.financialtracker.exception.CustomException;
import com.example.financialtracker.report.PerYearMonthCat;
import com.example.financialtracker.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public List<PerYearMonthCat> getPerMonthReport(long userId) {
        List<Map<String, Object>> reports = expenseRepository.findIncomePerMonthPerCat(userId);
        return new ArrayList<>(reports.stream().map(PerYearMonthCat::new).toList());
    }
    
    List<ExpenseResDto> getAllUserExpenses(long userId){
        List<Expense> expenses = expenseRepository.findExpensesByUser(userId);
        return new ArrayList<>(expenses.stream().map(ExpenseResDto::new).toList());
    }

    public ExpenseResDto getSingleExpense(long userId, long expenseId) {
        Optional<Expense> expense = expenseRepository.findSingleExpense(userId, expenseId);

        if (expense.isEmpty()) {
            throw new CustomException("Expense not found !", 404);
        }
        return new ExpenseResDto(expense.get());
    }

    public List<ExpenseResDto> getExpensesByCategory(long userId, long categoryId) {
        List<Expense> expenses = expenseRepository.findExpensesByCategory(userId, categoryId);
        return new ArrayList<>(expenses.stream().map(ExpenseResDto::new).toList());
    }

    public ExpenseResDto createExpense(ExpenseReqDto expenseReqDto, long userId) {
        User user = new User();
        user.setUserId(userId);
        Expense newExpense = new Expense(expenseReqDto, user);

        Expense savedExpense = expenseRepository.save(newExpense);

        return new ExpenseResDto(savedExpense);
    }

    public ExpenseResDto updateExpense(ExpenseReqDto expenseReqDto, long userId, long expenseId) {
        Optional<Expense> optExpense = expenseRepository.findSingleExpense(userId, expenseId);
        if (optExpense.isEmpty()) {
            throw new CustomException("Can not find expense at the moment !", 404);
        }
        Expense prevExpense = optExpense.get();

        ExpenseCategory expenseCategory = new ExpenseCategory();
        expenseCategory.setCategoryId(expenseReqDto.getCategoryId());
        prevExpense.setExpenseCategory(expenseCategory);
        prevExpense.setTitle(expenseReqDto.getTitle());
        prevExpense.setDescription(expenseReqDto.getDescription());
        prevExpense.setAmount(expenseReqDto.getAmount());

        Expense savedExpense = expenseRepository.save(prevExpense);
        return new ExpenseResDto(savedExpense);
    }

    public void removeExpense(long userId, long expenseId){
        Optional<Expense> prevExpense = expenseRepository.findSingleExpense(userId, expenseId);
        if(prevExpense.isEmpty()){
            throw new CustomException("Can not find expense at the moment !", 404);
        }

        expenseRepository.delete(prevExpense.get());
    }
}
