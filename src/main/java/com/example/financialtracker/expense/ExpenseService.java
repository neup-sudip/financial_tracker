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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    @Value("${confirm.cookie.value}")
    private String CONFIRM_COOKIE_VALUE;

    static final int LIMIT = 10;
    static final String SERVER_ERROR_MESSAGE = "Internal Server Error !";
    static final int SERVER_ERROR_CODE = 500;

    public List<PerYearMonthCat> getPerMonthReport(User user) {
        try {
            List<Map<String, Object>> reports = expenseRepository.findExpensePerMonthPerCat(user);
            return new ArrayList<>(reports.stream().map(PerYearMonthCat::new).toList());
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }

    public List<PerMonthCatExpense> getPerMonthCatExpense(long userId, long categoryId) {
        try {
            User user = new User(userId);
            ExpenseCategory category = new ExpenseCategory(categoryId);
            List<Map<String, Object>> reports = expenseRepository.findPerMonthCatExpense(user, category);
            return new ArrayList<>(reports.stream().map(PerMonthCatExpense::new).toList());
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }

    public List<ExpenseResDto> downloadExpense(long userId) {
        try {
            List<Expense> expenses = expenseRepository.downloadExpense(userId);
            return new ArrayList<>(expenses.stream().map(ExpenseResDto::new).toList());
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }

    public List<ExpenseResDto> getAllUserExpenses(long userId, int page) {
        try {
            PageRequest pageRequest = PageRequest.of(page - 1, LIMIT);
            Page<Expense> expenses = expenseRepository.findExpensesByUser(userId, pageRequest);
            return new ArrayList<>(expenses.getContent().stream().map(ExpenseResDto::new).toList());
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }

    public ExpenseResDto getSingleExpense(long userId, long expenseId) {
        Optional<Expense> expense;
        try {
            expense = expenseRepository.findSingleExpense(userId, expenseId);
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }

        if (expense.isEmpty()) {
            throw new CustomException("Expense not found !", 404);
        }
        return new ExpenseResDto(expense.get());
    }

    public List<ExpenseResDto> getExpensesByCategory(long userId, long categoryId) {
        try {
            List<Expense> expenses = expenseRepository.findExpensesByCategory(userId, categoryId);
            return new ArrayList<>(expenses.stream().map(ExpenseResDto::new).toList());
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }

    public ExpenseResDto createExpense(ExpenseReqDto expenseReqDto, long userId, boolean hasConfirm) {
        try {
            int i = 1;
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

        } catch (CustomException ex) {
            throw new CustomException(ex.getMessage(), ex.getStatus());
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }

    public ExpenseResDto updateExpense(ExpenseReqDto expenseReqDto, long userId, long expenseId, boolean hasConfirm) {
        try {
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
        } catch (CustomException ex) {
            throw new CustomException(ex.getMessage(), ex.getStatus());
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }

    public void removeExpense(long userId, long expenseId) {
        Optional<Expense> prevExpense;
        try {
            prevExpense = expenseRepository.findSingleExpense(userId, expenseId);
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }

        if (prevExpense.isEmpty()) {
            throw new CustomException("Can not find expense at the moment !", 404);
        }
        expenseRepository.delete(prevExpense.get());
    }

    private void handleYearLimit(long userId, ExpenseReqDto expenseReqDto, BigDecimal previousAmount) {
        try {
            int year = expenseReqDto.getDate().getYear();
            Optional<BigDecimal> optTotalUsed = expenseRepository.getTotalOfCatInYear(userId, expenseReqDto.getCategoryId(), year);
            Optional<Years> yearData = yearsService.getYearByCategoryAndYear(expenseReqDto.getCategoryId(), year);

            BigDecimal yearLimit = BigDecimal.valueOf(0);
            if (yearData.isPresent()) {
                yearLimit = yearData.get().getAmountLimit();
            }
            BigDecimal usedAmount = BigDecimal.valueOf(0);
            if (optTotalUsed.isPresent()) {
                usedAmount = optTotalUsed.get();
            }

            BigDecimal totalAmount = usedAmount.add(expenseReqDto.getAmount()).subtract(previousAmount);
            if (yearLimit.compareTo(totalAmount) < 0) {
                final Cookie cookie = new Cookie("confirm", CONFIRM_COOKIE_VALUE);
                cookie.setSecure(false);
                cookie.setHttpOnly(true);
                cookie.setMaxAge(CONFIRM_COOKIE_EXPIRE);
                cookie.setPath("/api");
                response.addCookie(cookie);
                throw new CustomException("Amount limit exceed !\n Do you want to continue ?", 201);
            }
        } catch (CustomException ex) {
            throw new CustomException(ex.getMessage(), ex.getStatus());
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
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
