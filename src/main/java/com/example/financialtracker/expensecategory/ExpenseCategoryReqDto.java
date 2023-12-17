package com.example.financialtracker.expensecategory;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExpenseCategoryReqDto {

    @NotEmpty(message = "Title is required !")
    @Size(min = 4, message = "title should have at least 4 characters")
    private String title;

    private String description;

    @DecimalMin(value = "100", message = "Amount Limit must be at least 100.")
    private BigDecimal amountLimit;

    @Positive(message = "Item Limit must be greater than 0 !")
    private int itemLimit;
}
