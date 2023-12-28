package com.example.financialtracker.incomecategory;

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
@RequestMapping("/api/v1/category/income")
public class IncomeCategoryController {

    private final IncomeCategoryService incomeCategoryService;
    private final HttpServletRequest request;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<IncomeCategoryResDto>>> getAllCategory(){
        User user = (User) request.getAttribute("user");
        List<IncomeCategoryResDto> incomeCategoryResDtos = incomeCategoryService.getAllCategoryByUser(user.getUserId());
        return ResponseEntity.status(200).body(new ApiResponse<>(true, incomeCategoryResDtos, "Categories fetched !"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<IncomeCategoryResDto>> getSingleCategory(@PathVariable long id){
        User user = (User) request.getAttribute("user");
        IncomeCategoryResDto incomeCategoryResDto = incomeCategoryService.getSingleCategory(user.getUserId(), id);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, incomeCategoryResDto, "Category fetched !"));
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<IncomeCategoryResDto>> createCategory(@Valid @RequestBody IncomeCategoryReqDto incomeCategoryReqDto){
        User user = (User) request.getAttribute("user");
        IncomeCategoryResDto incomeCategoryResDto = incomeCategoryService.createCategory(incomeCategoryReqDto, user.getUserId());
        return ResponseEntity.status(200).body(new ApiResponse<>(true, incomeCategoryResDto, "Category created !"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<IncomeCategoryResDto>> updateCategory(@Valid @RequestBody IncomeCategoryReqDto incomeCategoryReqDto, @PathVariable long id){
        User user = (User) request.getAttribute("user");
        IncomeCategoryResDto incomeCategoryResDto = incomeCategoryService.updateCategory(incomeCategoryReqDto, user.getUserId(), id);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, incomeCategoryResDto, "Category updated !"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> activateCategory(@RequestBody String action, @PathVariable long id){
        User user = (User) request.getAttribute("user");
        incomeCategoryService.updateCatStatus(user.getUserId(), id, action.replace("\"", ""));
        return ResponseEntity.status(200).body(new ApiResponse<>(true, "", "Status updated !"));
    }
}
