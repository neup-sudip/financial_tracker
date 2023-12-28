package com.example.financialtracker.income;

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
@RequestMapping("/api/v1/income")
public class IncomeController {

    private final IncomeService incomeService;

    private final HttpServletRequest request;

    @GetMapping("/download")
    public ResponseEntity<ApiResponse<List<IncomeResDto>>> downloadExpense(){
        User user = (User) request.getAttribute("user");
        List<IncomeResDto> incomeResDtos = incomeService.downloadExpense(user.getUserId());
        return ResponseEntity.status(200).body(new ApiResponse<>(true, incomeResDtos, "Expenses fetched !"));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<IncomeResDto>>> getAllIncome(@RequestParam(name = "page", defaultValue = "1") int page){
        User user = (User) request.getAttribute("user");
        List<IncomeResDto> incomeResDtos = incomeService.getAllUserIncomes(user.getUserId(), page);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, incomeResDtos, "Incomes fetched !"));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<ApiResponse<List<IncomeResDto>>> getIncomesByCategory(@PathVariable long id ){
        User user = (User) request.getAttribute("user");
        List<IncomeResDto> incomeResDtos = incomeService.getIncomesByCategory(user.getUserId(), id);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, incomeResDtos, "Category incomes fetched !"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<IncomeResDto>> getSingleIncome(@PathVariable long id ){
        User user = (User) request.getAttribute("user");
        IncomeResDto incomeResDto = incomeService.getSingleIncome(user.getUserId(), id);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, incomeResDto, "Income fetched !"));
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<IncomeResDto>> createIncome(@Valid @RequestBody IncomeReqDto incomeReqDto ){
        User user = (User) request.getAttribute("user");
        IncomeResDto incomeResDto = incomeService.createIncome(incomeReqDto, user.getUserId());
        return ResponseEntity.status(200).body(new ApiResponse<>(true, incomeResDto, "Income created !"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<IncomeResDto>> updateIncome(@Valid @RequestBody IncomeReqDto incomeReqDto, @PathVariable long id ){
        User user = (User) request.getAttribute("user");
        IncomeResDto incomeResDto = incomeService.updateIncome(incomeReqDto, user.getUserId(), id);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, incomeResDto, "Income updated !"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> removeIncome(@PathVariable long id ){
        User user = (User) request.getAttribute("user");
        incomeService.removeIncome(user.getUserId(), id);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, "", "Income deleted !"));
    }
}

