package com.example.financialtracker.income;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IncomeReqDto {
    @Positive(message = "Invalid category !")
    private long categoryId;

    @NotEmpty(message = "Title is required !")
    private String title;

    @NotEmpty(message = "Description is required !")
    private String description;

    @DecimalMin(value = "1", message = "Amount Limit must be at least 1.")
    private BigDecimal amount;
}
