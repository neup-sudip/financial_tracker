package com.example.financialtracker.expensecategory;

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
@RequestMapping("/api/v1/category/expense")
public class ExpenseCategoryController {

    private final ExpenseCategoryService expenseCategoryService;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<ExpenseCategoryResDto>>> getAllCategory(HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        List<ExpenseCategoryResDto> expenseCategoryResDtos = expenseCategoryService.getAllCategoryByUser(user.getUserId());
        return ResponseEntity.status(200).body(new ApiResponse<>(true, expenseCategoryResDtos, "Categories fetched !"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseCategoryResDto>> getSingleCategory(@PathVariable long id, HttpServletRequest request){
        User user = (User) request.getAttribute("user");

        ExpenseCategoryResDto expenseCategoryResDto = expenseCategoryService.getSingleCategory(user.getUserId(), id);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, expenseCategoryResDto, "Category fetched !"));
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<ExpenseCategoryResDto>> createCategory(@Valid @RequestBody ExpenseCategoryReqDto expenseCategoryReqDto, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        ExpenseCategoryResDto expenseCategoryResDto = expenseCategoryService.createCategory(expenseCategoryReqDto, user.getUserId());
        return ResponseEntity.status(200).body(new ApiResponse<>(true, expenseCategoryResDto, "Category created !"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseCategoryResDto>> updateCategory(@Valid @RequestBody ExpenseCategoryReqDto expenseCategoryReqDto, @PathVariable long id, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        ExpenseCategoryResDto expenseCategoryResDto = expenseCategoryService.updateCategory(expenseCategoryReqDto, user.getUserId(), id);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, expenseCategoryResDto, "Category updated !"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> activateCategory(@RequestBody String action, @PathVariable long id, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        expenseCategoryService.updateCatStatus(user.getUserId(), id, action.replace("\"", ""));
        return ResponseEntity.status(200).body(new ApiResponse<>(true, "", "Status updated !"));
    }
}
