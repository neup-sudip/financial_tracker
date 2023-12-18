package com.example.financialtracker.income;

import com.example.financialtracker.exception.CustomException;
import com.example.financialtracker.incomecategory.IncomeCategory;
import com.example.financialtracker.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IncomeService {
    private final IncomeRepository incomeRepository;

    public List<Map<String, String>>getPerMonthReport(long userId){
        return incomeRepository.findIncomePerMonthPerCat(userId);
    }

    List<IncomeResDto> getAllUserIncomes(long userId){
        List<Income> incomes = incomeRepository.findIncomesByUser(userId);
        return new ArrayList<>(incomes.stream().map(IncomeResDto::new).toList());
    }

    public IncomeResDto getSingleIncome(long userId, long incomeId) {
        Optional<Income> income = incomeRepository.findSingleIncome(userId, incomeId);

        if (income.isEmpty()) {
            throw new CustomException("Income not found !", 404);
        }
        return new IncomeResDto(income.get());
    }

    public List<IncomeResDto> getIncomesByCategory(long userId, long categoryId) {
        List<Income> incomes = incomeRepository.findIncomesByCategory(userId, categoryId);
        return new ArrayList<>(incomes.stream().map(IncomeResDto::new).toList());
    }

    public IncomeResDto createIncome(IncomeReqDto incomeReqDto, long userId) {
        User user = new User();
        user.setUserId(userId);
        Income newIncome = new Income(incomeReqDto, user);

        Income savedIncome = incomeRepository.save(newIncome);

        return new IncomeResDto(savedIncome);
    }

    public IncomeResDto updateIncome(IncomeReqDto incomeReqDto, long userId, long incomeId) {
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

        Income savedIncome = incomeRepository.save(prevIncome);
        return new IncomeResDto(savedIncome);
    }

    public void removeIncome(long userId, long incomeId){
        Optional<Income> prevIncome = incomeRepository.findSingleIncome(userId, incomeId);
        if(prevIncome.isEmpty()){
            throw new CustomException("Can not find income at the moment !", 404);
        }

        incomeRepository.delete(prevIncome.get());
    }
}
