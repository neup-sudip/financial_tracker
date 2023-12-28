package com.example.financialtracker.income;

import com.example.financialtracker.exception.CustomException;
import com.example.financialtracker.incomecategory.IncomeCategory;
import com.example.financialtracker.report.PerYearMonthCat;
import com.example.financialtracker.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class IncomeService {
    private final IncomeRepository incomeRepository;

    static final int LIMIT = 10;
    static final String SERVER_ERROR_MESSAGE = "Internal Server Error !";
    static final int SERVER_ERROR_CODE = 500;

    public List<PerYearMonthCat> getPerMonthReport(User user) {
        try {
            List<Map<String, Object>> reports = incomeRepository.findIncomePerMonthPerCat(user);
            return new ArrayList<>(reports.stream().map(PerYearMonthCat::new).toList());
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }

    public List<IncomeResDto> downloadExpense(long userId) {
        try {
            List<Income> incomes = incomeRepository.downloadExpense(userId);
            return new ArrayList<>(incomes.stream().map(IncomeResDto::new).toList());
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }

    public List<IncomeResDto> getAllUserIncomes(long userId, int page) {
        try {
            PageRequest pageRequest = PageRequest.of(page - 1, LIMIT);
            Page<Income> incomes = incomeRepository.findIncomesByUser(userId, pageRequest);
            return new ArrayList<>(incomes.getContent().stream().map(IncomeResDto::new).toList());
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }

    public IncomeResDto getSingleIncome(long userId, long incomeId) {
        try {
            Optional<Income> income = incomeRepository.findSingleIncome(userId, incomeId);
            if (income.isEmpty()) {
                throw new CustomException("Income not found !", 404);
            }
            return new IncomeResDto(income.get());
        } catch (CustomException ex) {
            throw new CustomException(ex.getMessage(), ex.getStatus());
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }

    public List<IncomeResDto> getIncomesByCategory(long userId, long categoryId) {
        try {
            List<Income> incomes = incomeRepository.findIncomesByCategory(userId, categoryId);
            return new ArrayList<>(incomes.stream().map(IncomeResDto::new).toList());
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }

    public IncomeResDto createIncome(IncomeReqDto incomeReqDto, long userId) {
        try {
            User user = new User(userId);
            Income newIncome = new Income(incomeReqDto, user);
            Income savedIncome = incomeRepository.save(newIncome);
            return new IncomeResDto(savedIncome);
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }

    public IncomeResDto updateIncome(IncomeReqDto incomeReqDto, long userId, long incomeId) {
        try {
            Optional<Income> optIncome = incomeRepository.findSingleIncome(userId, incomeId);
            if (optIncome.isEmpty()) {
                throw new CustomException("Can not find income at the moment !", 404);
            }
            Income prevIncome = optIncome.get();

            IncomeCategory incomeCategory = new IncomeCategory();
            incomeCategory.setCategoryId(incomeReqDto.getCategoryId());
            prevIncome.setIncomeCategory(incomeCategory);
            prevIncome.setTitle(incomeReqDto.getTitle());
            prevIncome.setDescription(incomeReqDto.getDescription());
            prevIncome.setAmount(incomeReqDto.getAmount());
            prevIncome.setCreatedOn(incomeReqDto.getDate());

            Income savedIncome = incomeRepository.save(prevIncome);
            return new IncomeResDto(savedIncome);
        } catch (CustomException ex) {
            throw new CustomException(ex.getMessage(), ex.getStatus());
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }

    public void removeIncome(long userId, long incomeId) {
        try {
            Optional<Income> prevIncome = incomeRepository.findSingleIncome(userId, incomeId);
            if (prevIncome.isEmpty()) {
                throw new CustomException("Can not find income at the moment !", 404);
            }
            incomeRepository.delete(prevIncome.get());
        } catch (CustomException ex) {
            throw new CustomException(ex.getMessage(), ex.getStatus());
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }
}
