package com.example.financialtracker.incomecategory;

import com.example.financialtracker.exception.CustomException;
import com.example.financialtracker.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IncomeCategoryService {
    private final IncomeCategoryRepository incomeCategoryRepository;

    public List<IncomeCategoryResDto> getAllCategoryByUser(long userId) {
        List<IncomeCategory> categories = incomeCategoryRepository.findAllCategory(userId);
        return new ArrayList<>(categories.stream().map(IncomeCategoryResDto::new).toList());
    }

    public IncomeCategoryResDto getSingleCategory(long userId, long categoryId) {
        Optional<IncomeCategory> category = incomeCategoryRepository.findSingleCategory(categoryId, userId);

        if (category.isEmpty()) {
            throw new CustomException("Category not found !", 404);
        }
        return new IncomeCategoryResDto(category.get());
    }

    public IncomeCategoryResDto createCategory(IncomeCategoryReqDto incomeCategoryReqDto, long userId) {
        Optional<IncomeCategory> titleExist = incomeCategoryRepository.findCategoryByTitle(incomeCategoryReqDto.getTitle(), userId);

        if (titleExist.isPresent()) {
            throw new CustomException("You already have category with this title", 400);
        }

        User user = new User(userId);
        IncomeCategory newIncomeCategory = new IncomeCategory(incomeCategoryReqDto, user);

        IncomeCategory savedIncomeCategory = incomeCategoryRepository.save(newIncomeCategory);

        return new IncomeCategoryResDto(savedIncomeCategory);
    }

    public IncomeCategoryResDto updateCategory(IncomeCategoryReqDto incomeCategoryReqDto, long userId, long categoryId) {
        Optional<IncomeCategory> titleExist = incomeCategoryRepository.findByTitleAndNotId(incomeCategoryReqDto.getTitle(), userId, categoryId);
        if (titleExist.isPresent()) {
            throw new CustomException("You already have category with this title", 400);
        }

        Optional<IncomeCategory> prevCat = incomeCategoryRepository.findSingleCategory(categoryId, userId);
        if (prevCat.isEmpty()) {
            throw new CustomException("Can not find category at the moment !", 404);
        }
        IncomeCategory preIncomeCategory = prevCat.get();

        preIncomeCategory.setTitle(incomeCategoryReqDto.getTitle());
        preIncomeCategory.setDescription(incomeCategoryReqDto.getDescription());

        IncomeCategory savedIncomeCategory = incomeCategoryRepository.save(preIncomeCategory);
        return new IncomeCategoryResDto(savedIncomeCategory);
    }

    public void removeCategory(long userId, long categoryId){
        Optional<IncomeCategory> prevCat = incomeCategoryRepository.findSingleCategory(categoryId, userId);
        if(prevCat.isEmpty()){
            throw new CustomException("Can not find category at the moment !", 404);
        }

        IncomeCategory prevIncomeCategory = prevCat.get();
        prevIncomeCategory.setStatus(false);

        incomeCategoryRepository.save(prevIncomeCategory);
    }
}