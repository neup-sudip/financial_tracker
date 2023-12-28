package com.example.financialtracker.years;

import com.example.financialtracker.wrapper.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/expense-year")
public class YearsController {

    private final YearsService yearsService;

    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<YearsResDto>> addYears(@RequestBody YearsReqDto yearsReqDto, @PathVariable long id) {
        YearsResDto yearsResDto = yearsService.addNewYear(yearsReqDto, id);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, yearsResDto, "Successfully added !"));
    }

    @PutMapping("/{catId}/{yearId}")
    public ResponseEntity<ApiResponse<YearsResDto>> editYears(@RequestBody YearsReqDto yearsReqDto, @PathVariable long catId, @PathVariable long yearId) {
        YearsResDto yearsResDto = yearsService.editYear(yearsReqDto, catId, yearId);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, yearsResDto, "Successfully updated !"));
    }
}
