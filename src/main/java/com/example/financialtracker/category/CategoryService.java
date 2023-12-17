package com.example.financialtracker.category;

import com.example.financialtracker.exception.CustomException;
import com.example.financialtracker.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryResDto> getAllCategoryByUser(long userId) {
        List<Category> categories = categoryRepository.findCategoryByUserId(userId);
        return new ArrayList<>(categories.stream().map(CategoryResDto::new).toList());
    }

    public CategoryResDto getSingleCategory(long userId, long categoryId) {
        Optional<Category> category = categoryRepository.findCategoryByIdAndUserId(categoryId, userId);

        if (category.isEmpty()) {
            throw new CustomException("Category not found !", 404);
        }
        return new CategoryResDto(category.get());
    }

    public CategoryResDto createCategory(CategoryReqDto categoryReqDto, long userId) {
        Optional<Category> titleExist = categoryRepository.findCategoryByTitleAndUserId(categoryReqDto.getTitle(), userId);

        if (titleExist.isPresent()) {
            throw new CustomException("You already have category with this title", 400);
        }

        User user = new User();
        user.setUserId(userId);
        Category newCategory = new Category(categoryReqDto, user);

        Category savedCategory = categoryRepository.save(newCategory);

        return new CategoryResDto(savedCategory);
    }

    public CategoryResDto updateCategory(CategoryReqDto categoryReqDto, long userId, long categoryId) {
        Optional<Category> titleExist = categoryRepository.findByTitleAndUserIdNotId(categoryReqDto.getTitle(), userId, categoryId);
        if (titleExist.isPresent()) {
            throw new CustomException("You already have category with this title", 400);
        }

        Optional<Category> prevCat = categoryRepository.findCategoryByIdAndUserId(categoryId, userId);
        if (prevCat.isEmpty()) {
            throw new CustomException("Can not find category at the moment !", 404);
        }
        Category preCategory = prevCat.get();

        preCategory.setTitle(categoryReqDto.getTitle());
        preCategory.setDescription(categoryReqDto.getDescription());
        preCategory.setAmountLimit(categoryReqDto.getAmountLimit());
        preCategory.setItemLimit(categoryReqDto.getItemLimit());

        Category savedCategory = categoryRepository.save(preCategory);
        return new CategoryResDto(savedCategory);
    }

    public void removeCategory(long userId, long categoryId){
        Optional<Category> prevCat = categoryRepository.findCategoryByIdAndUserId(categoryId, userId);
        if(prevCat.isEmpty()){
            throw new CustomException("Can not find category at the moment !", 404);
        }

        Category prevCategory = prevCat.get();
        prevCategory.setStatus(false);

        categoryRepository.save(prevCategory);
    }
}
