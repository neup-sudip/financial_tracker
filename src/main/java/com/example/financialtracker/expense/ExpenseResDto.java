package com.example.financialtracker.expense;

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
public class ExpenseResDto {
    private long expenseId;

    private long categoryId;

    private String categoryTitle;

    private String expenseTitle;

    private String description;

    private BigDecimal amount;

    private LocalDate createdOn;

    public ExpenseResDto(Expense expense) {
        this.expenseId = expense.getExpenseId();
        this.categoryId = expense.getCategory().getCategoryId();
        this.categoryTitle = expense.getCategory().getTitle();
        this.expenseTitle = expense.getTitle();
        this.description = expense.getDescription();
        this.amount = expense.getAmount();
        this.createdOn = expense.getCreatedOn();
    }
}
