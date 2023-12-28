package com.example.financialtracker.expensecategory;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExpenseCategoryReqDto {

    @NotEmpty(message = "Title is required !")
    @Size(min = 4, message = "title should have at least 4 characters")
    private String title;

    @NotEmpty(message = "Title is required !")
    @Size(min = 20, message = "title should have at least 20 characters")
    private String description;
}
