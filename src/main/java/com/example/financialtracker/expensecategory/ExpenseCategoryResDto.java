package com.example.financialtracker.expensecategory;

import com.example.financialtracker.years.YearsResDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExpenseCategoryResDto {
    private long id;

    private String title;

    private String description;

    private List<YearsResDto> years = new ArrayList<>();

    private LocalDateTime createdOn;

    private boolean status;

    public ExpenseCategoryResDto(ExpenseCategory expenseCategory) {
        this.id = expenseCategory.getCategoryId();
        this.title = expenseCategory.getTitle();
        this.description = expenseCategory.getDescription();
        this.createdOn = expenseCategory.getCreatedOn();
        this.status = expenseCategory.isStatus();
    }

    public ExpenseCategoryResDto(ExpenseCategory expenseCategory, List<YearsResDto> yearsResDtos) {
        this.id = expenseCategory.getCategoryId();
        this.title = expenseCategory.getTitle();
        this.description = expenseCategory.getDescription();
        this.years = yearsResDtos;
        this.status = expenseCategory.isStatus();
    }
}
