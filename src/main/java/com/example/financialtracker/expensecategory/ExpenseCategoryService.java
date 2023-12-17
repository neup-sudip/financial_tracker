package com.example.financialtracker.expensecategory;

import com.example.financialtracker.exception.CustomException;
import com.example.financialtracker.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ExpenseCategoryService {
    private final ExpenseCategoryRepository expenseCategoryRepository;

    public List<ExpenseCategoryResDto> getAllCategoryByUser(long userId) {
        List<ExpenseCategory> categories = expenseCategoryRepository.findCategoryByUserId(userId);
        return new ArrayList<>(categories.stream().map(ExpenseCategoryResDto::new).toList());
    }

    public ExpenseCategoryResDto getSingleCategory(long userId, long categoryId) {
        Optional<ExpenseCategory> category = expenseCategoryRepository.findCategoryByIdAndUserId(categoryId, userId);

        if (category.isEmpty()) {
            throw new CustomException("Category not found !", 404);
        }
        return new ExpenseCategoryResDto(category.get());
    }

    public ExpenseCategoryResDto createCategory(ExpenseCategoryReqDto expenseCategoryReqDto, long userId) {
        Optional<ExpenseCategory> titleExist = expenseCategoryRepository.findCategoryByTitleAndUserId(expenseCategoryReqDto.getTitle(), userId);

        if (titleExist.isPresent()) {
            throw new CustomException("You already have category with this title", 400);
        }

        User user = new User();
        user.setUserId(userId);
        ExpenseCategory newExpenseCategory = new ExpenseCategory(expenseCategoryReqDto, user);

        ExpenseCategory savedExpenseCategory = expenseCategoryRepository.save(newExpenseCategory);

        return new ExpenseCategoryResDto(savedExpenseCategory);
    }

    public ExpenseCategoryResDto updateCategory(ExpenseCategoryReqDto expenseCategoryReqDto, long userId, long categoryId) {
        Optional<ExpenseCategory> titleExist = expenseCategoryRepository.findByTitleAndUserIdNotId(expenseCategoryReqDto.getTitle(), userId, categoryId);
        if (titleExist.isPresent()) {
            throw new CustomException("You already have category with this title", 400);
        }

        Optional<ExpenseCategory> prevCat = expenseCategoryRepository.findCategoryByIdAndUserId(categoryId, userId);
        if (prevCat.isEmpty()) {
            throw new CustomException("Can not find category at the moment !", 404);
        }
        ExpenseCategory preExpenseCategory = prevCat.get();

        preExpenseCategory.setTitle(expenseCategoryReqDto.getTitle());
        preExpenseCategory.setDescription(expenseCategoryReqDto.getDescription());
        preExpenseCategory.setAmountLimit(expenseCategoryReqDto.getAmountLimit());
        preExpenseCategory.setItemLimit(expenseCategoryReqDto.getItemLimit());

        ExpenseCategory savedExpenseCategory = expenseCategoryRepository.save(preExpenseCategory);
        return new ExpenseCategoryResDto(savedExpenseCategory);
    }

    public void removeCategory(long userId, long categoryId){
        Optional<ExpenseCategory> prevCat = expenseCategoryRepository.findCategoryByIdAndUserId(categoryId, userId);
        if(prevCat.isEmpty()){
            throw new CustomException("Can not find category at the moment !", 404);
        }

        ExpenseCategory prevExpenseCategory = prevCat.get();
        prevExpenseCategory.setStatus(false);

        expenseCategoryRepository.save(prevExpenseCategory);
    }
}
