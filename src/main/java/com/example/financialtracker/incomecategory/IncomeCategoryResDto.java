package com.example.financialtracker.incomecategory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IncomeCategoryResDto {
    private long categoryId;

    private String title;

    private String description;

    private LocalDate createdOn;

    private boolean status;

    public IncomeCategoryResDto(IncomeCategory incomeCategory) {
        this.categoryId = incomeCategory.getCategoryId();
        this.title = incomeCategory.getTitle();
        this.description = incomeCategory.getDescription();
        this.createdOn = incomeCategory.getCreatedOn();
        this.status = incomeCategory.isStatus();
    }
}
