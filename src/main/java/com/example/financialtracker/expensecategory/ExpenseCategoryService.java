package com.example.financialtracker.expensecategory;

import com.example.financialtracker.exception.CustomException;
import com.example.financialtracker.user.User;
import com.example.financialtracker.years.YearsResDto;
import com.example.financialtracker.years.YearsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ExpenseCategoryService {
    private final ExpenseCategoryRepository expenseCategoryRepository;

    private final YearsService yearsService;

    public List<ExpenseCategoryResDto> getAllCategoryByUser(long userId) {
        List<ExpenseCategory> categories = expenseCategoryRepository.findAllCategories(userId);

        return new ArrayList<>(categories.stream().map(ExpenseCategoryResDto::new).toList());
    }

    public ExpenseCategoryResDto getSingleCategory(long userId, long categoryId) {
        Optional<ExpenseCategory> category = expenseCategoryRepository.findCategoryById(categoryId, userId);

        if (category.isEmpty()) {
            throw new CustomException("Category not found !", 404);
        }
        List<YearsResDto> yearsResDtos = yearsService.getAllYearsByCategory(categoryId);
        return new ExpenseCategoryResDto(category.get(), yearsResDtos);
    }

    public ExpenseCategoryResDto createCategory(ExpenseCategoryReqDto expenseCategoryReqDto, long userId) {
        Optional<ExpenseCategory> titleExist = expenseCategoryRepository.findCategoryByTitle(expenseCategoryReqDto.getTitle(), userId);

        if (titleExist.isPresent()) {
            throw new CustomException("You already have category with this title", 400);
        }

        User user = new User(userId);
        ExpenseCategory newExpenseCategory = new ExpenseCategory(expenseCategoryReqDto, user);

        ExpenseCategory savedExpenseCategory = expenseCategoryRepository.save(newExpenseCategory);

        return new ExpenseCategoryResDto(savedExpenseCategory);
    }

    public ExpenseCategoryResDto updateCategory(ExpenseCategoryReqDto expenseCategoryReqDto, long userId, long categoryId) {
        Optional<ExpenseCategory> titleExist = expenseCategoryRepository.findByTitleAndNotId(expenseCategoryReqDto.getTitle(), userId, categoryId);
        if (titleExist.isPresent()) {
            throw new CustomException("You already have category with this title", 400);
        }

        Optional<ExpenseCategory> prevCat = expenseCategoryRepository.findCategoryById(categoryId, userId);
        if (prevCat.isEmpty()) {
            throw new CustomException("Can not find category at the moment !", 404);
        }
        ExpenseCategory preExpenseCategory = prevCat.get();

        preExpenseCategory.setTitle(expenseCategoryReqDto.getTitle());
        preExpenseCategory.setDescription(expenseCategoryReqDto.getDescription());

        ExpenseCategory savedExpenseCategory = expenseCategoryRepository.save(preExpenseCategory);
        return new ExpenseCategoryResDto(savedExpenseCategory);
    }

    public void updateCatStatus(long userId, long categoryId, String action){
        Optional<ExpenseCategory> prevCat = expenseCategoryRepository.findCategoryById(categoryId, userId);
        if(prevCat.isEmpty()){
            throw new CustomException("Can not find category at the moment !", 404);
        }
        ExpenseCategory prevExpenseCategory = prevCat.get();
        if(action.equals("A")){
            prevExpenseCategory.setStatus(true);
        }

        if(action.equals("R")){
            prevExpenseCategory.setStatus(false);
        }

        expenseCategoryRepository.save(prevExpenseCategory);
    }
}
