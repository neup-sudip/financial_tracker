package com.example.financialtracker.category;

import com.example.financialtracker.user.User;
import com.example.financialtracker.wrapper.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<CategoryResDto>>> getAllCategory(HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        List<CategoryResDto> categoryResDtos = categoryService.getAllCategoryByUser(user.getUserId());
        return ResponseEntity.status(200).body(new ApiResponse<>(true, categoryResDtos, "Categories fetched !"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResDto>> getSingleCategory(@PathVariable long id, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        CategoryResDto categoryResDto = categoryService.getSingleCategory(user.getUserId(), id);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, categoryResDto, "Category fetched !"));
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<CategoryResDto>> createCategory(@Valid @RequestBody CategoryReqDto categoryReqDto, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        CategoryResDto categoryResDto = categoryService.createCategory(categoryReqDto, user.getUserId());
        return ResponseEntity.status(200).body(new ApiResponse<>(true, categoryResDto, "Category created !"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResDto>> updateCategory(@Valid @RequestBody CategoryReqDto categoryReqDto, @PathVariable long id, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        CategoryResDto categoryResDto = categoryService.updateCategory(categoryReqDto, user.getUserId(), id);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, categoryResDto, "Category updated !"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> removeCategory(@PathVariable long id, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        categoryService.removeCategory(user.getUserId(), id);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, "", "Category removed !"));
    }
}
