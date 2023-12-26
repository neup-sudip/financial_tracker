package com.example.financialtracker.expense;

import com.example.financialtracker.expensecategory.ExpenseCategory;
import com.example.financialtracker.exception.CustomException;
import com.example.financialtracker.report.PerMonthCatExpense;
import com.example.financialtracker.report.PerYearMonthCat;
import com.example.financialtracker.user.User;
import com.example.financialtracker.years.Years;
import com.example.financialtracker.years.YearsService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    private final YearsService yearsService;

    private final HttpServletResponse response;

    @Value("${confirm.cookie.expire}")
    private int CONFIRM_COOKIE_EXPIRE;

    private final int totalBooksPerPage = 2;

    public List<PerYearMonthCat> getPerMonthReport(User user) {
        List<Map<String, Object>> reports = expenseRepository.findExpensePerMonthPerCat(user);
        return new ArrayList<>(reports.stream().map(PerYearMonthCat::new).toList());
    }

    public List<PerMonthCatExpense> getPerMonthCatExpense(long userId, long categoryId) {
        User user = new User(userId);
        ExpenseCategory category = new ExpenseCategory(categoryId);
        List<Map<String, Object>> reports = expenseRepository.findPerMonthCatExpense(user, category);
        return new ArrayList<>(reports.stream().map(PerMonthCatExpense::new).toList());
    }

    List<ExpenseResDto> getAllUserExpenses(long userId, long categoryId, String query) {
        List<Expense> expenses;
        User user = new User(userId);
        if (categoryId > 0) {
            expenses = expenseRepository.findByUserAndCategory(userId, categoryId);
        } else {
            expenses = expenseRepository.findExpensesByUser(userId);
        }
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

    public ExpenseResDto createExpense(ExpenseReqDto expenseReqDto, long userId, boolean hasConfirm) {
        User user = new User(userId);
        Expense newExpense = new Expense(expenseReqDto, user);
        if (hasConfirm) {
            Expense savedExpense = expenseRepository.save(newExpense);
            removeCookie();
            return new ExpenseResDto(savedExpense);
        } else {
            handleYearLimit(userId, expenseReqDto, BigDecimal.valueOf(0));
            Expense savedExpense = expenseRepository.save(newExpense);
            return new ExpenseResDto(savedExpense);
        }
    }

    public ExpenseResDto updateExpense(ExpenseReqDto expenseReqDto, long userId, long expenseId, boolean hasConfirm) {
        Optional<Expense> optExpense = expenseRepository.findSingleExpense(userId, expenseId);
        if (optExpense.isEmpty()) {
            throw new CustomException("Can not find expense at the moment !", 404);
        }

        Expense prevExpense = optExpense.get();
        BigDecimal previousAmount = optExpense.get().getAmount();
        ExpenseCategory expenseCategory = new ExpenseCategory();
        expenseCategory.setCategoryId(expenseReqDto.getCategoryId());
        prevExpense.setExpenseCategory(expenseCategory);
        prevExpense.setTitle(expenseReqDto.getTitle());
        prevExpense.setDescription(expenseReqDto.getDescription());
        prevExpense.setAmount(expenseReqDto.getAmount());
        prevExpense.setCreatedOn(expenseReqDto.getDate());

        if (hasConfirm) {
            Expense savedExpense = expenseRepository.save(prevExpense);
            removeCookie();
            return new ExpenseResDto(savedExpense);
        } else {
            handleYearLimit(userId, expenseReqDto, previousAmount);
            Expense savedExpense = expenseRepository.save(prevExpense);
            return new ExpenseResDto(savedExpense);
        }
    }

    public void removeExpense(long userId, long expenseId) {
        Optional<Expense> prevExpense = expenseRepository.findSingleExpense(userId, expenseId);
        if (prevExpense.isEmpty()) {
            throw new CustomException("Can not find expense at the moment !", 404);
        }
        expenseRepository.delete(prevExpense.get());
    }

    private void handleYearLimit(long userId, ExpenseReqDto expenseReqDto, BigDecimal previousAmount) {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        BigDecimal totalUsed = expenseRepository.getTotalOfCatInYear(userId, expenseReqDto.getCategoryId(), year);
        Optional<Years> yearData = yearsService.getYearByCategoryAndYear(expenseReqDto.getCategoryId(), year);
        if (yearData.isPresent()) {
            BigDecimal totalAmount = totalUsed.add(expenseReqDto.getAmount()).subtract(previousAmount);
            if (yearData.get().getAmountLimit().compareTo(totalAmount) < 0) {
                final Cookie cookie = new Cookie("confirm", "true");
                cookie.setSecure(false);
                cookie.setHttpOnly(true);
                cookie.setMaxAge(CONFIRM_COOKIE_EXPIRE);
                cookie.setPath("/api");
                response.addCookie(cookie);
                throw new CustomException("Amount limit exceed !\n Do you want to continue ?", 201);
            }
        }
    }

    private void removeCookie() {
        final Cookie cookie = new Cookie("confirm", "true");
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/api");
        response.addCookie(cookie);
    }
}
