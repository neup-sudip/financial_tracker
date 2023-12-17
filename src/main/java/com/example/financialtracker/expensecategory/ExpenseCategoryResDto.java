package com.example.financialtracker.expensecategory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExpenseCategoryResDto {
    private long categoryId;

    private String title;

    private String description;

    private BigDecimal amountLimit;

    private int itemLimit;

    private LocalDate createdOn;

    private boolean status;

    public ExpenseCategoryResDto(ExpenseCategory expenseCategory) {
        this.categoryId = expenseCategory.getCategoryId();
        this.title = expenseCategory.getTitle();
        this.description = expenseCategory.getDescription();
        this.amountLimit = expenseCategory.getAmountLimit();
        this.itemLimit= expenseCategory.getItemLimit();
        this.createdOn = expenseCategory.getCreatedOn();
        this.status = expenseCategory.isStatus();
    }
}
