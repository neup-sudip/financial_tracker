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
    static final String SERVER_ERROR_MESSAGE = "Internal Server Error !";
    static final int SERVER_ERROR_CODE = 500;

    public List<IncomeCategoryResDto> getAllCategoryByUser(long userId) {
        try {
            List<IncomeCategory> categories = incomeCategoryRepository.findAllCategory(userId);
            return new ArrayList<>(categories.stream().map(IncomeCategoryResDto::new).toList());
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }

    public IncomeCategoryResDto getSingleCategory(long userId, long categoryId) {
        try {
            Optional<IncomeCategory> category = incomeCategoryRepository.findSingleCategory(categoryId, userId);
            if (category.isEmpty()) {
                throw new CustomException("Category not found !", 404);
            }
            return new IncomeCategoryResDto(category.get());
        } catch (CustomException ex) {
            throw new CustomException(ex.getMessage(), ex.getStatus());
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }

    public IncomeCategoryResDto createCategory(IncomeCategoryReqDto incomeCategoryReqDto, long userId) {
        try {
            Optional<IncomeCategory> titleExist = incomeCategoryRepository.findCategoryByTitle(incomeCategoryReqDto.getTitle(), userId);
            if (titleExist.isPresent()) {
                throw new CustomException("You already have category with this title", 400);
            }

            User user = new User(userId);
            IncomeCategory newIncomeCategory = new IncomeCategory(incomeCategoryReqDto, user);
            IncomeCategory savedIncomeCategory = incomeCategoryRepository.save(newIncomeCategory);
            return new IncomeCategoryResDto(savedIncomeCategory);
        } catch (CustomException ex) {
            throw new CustomException(ex.getMessage(), ex.getStatus());
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }

    public IncomeCategoryResDto updateCategory(IncomeCategoryReqDto incomeCategoryReqDto, long userId, long categoryId) {
        try {
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
        } catch (CustomException ex) {
            throw new CustomException(ex.getMessage(), ex.getStatus());
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }

    public void updateCatStatus(long userId, long categoryId, String action) {
        try {
            Optional<IncomeCategory> prevCat = incomeCategoryRepository.findSingleCategory(categoryId, userId);
            if (prevCat.isEmpty()) {
                throw new CustomException("Can not find category at the moment !", 404);
            }
            IncomeCategory prevIncomeCategory = prevCat.get();
            if (action.equals("A")) {
                prevIncomeCategory.setStatus(true);
            }
            if (action.equals("R")) {
                prevIncomeCategory.setStatus(false);
            }
            incomeCategoryRepository.save(prevIncomeCategory);
        } catch (CustomException ex) {
            throw new CustomException(ex.getMessage(), ex.getStatus());
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }
}