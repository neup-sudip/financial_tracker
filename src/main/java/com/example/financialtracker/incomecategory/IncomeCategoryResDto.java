package com.example.financialtracker.incomecategory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IncomeCategoryResDto {
    private long id;

    private String title;

    private String description;

    private LocalDateTime createdOn;

    private boolean status;

    public IncomeCategoryResDto(IncomeCategory incomeCategory) {
        this.id = incomeCategory.getCategoryId();
        this.title = incomeCategory.getTitle();
        this.description = incomeCategory.getDescription();
        this.createdOn = incomeCategory.getCreatedOn();
        this.status = incomeCategory.isStatus();
    }
}
