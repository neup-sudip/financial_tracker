package com.example.financialtracker.expense;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExpenseResDto {
    private long id;

    private long categoryId;

    private String categoryTitle;

    private String expenseTitle;

    private String description;

    private BigDecimal amount;

    private LocalDateTime createdOn;

    public ExpenseResDto(Expense expense) {
        this.id = expense.getExpenseId();
        this.categoryId = expense.getExpenseCategory().getCategoryId();
        this.categoryTitle = expense.getExpenseCategory().getTitle();
        this.expenseTitle = expense.getTitle();
        this.description = expense.getDescription();
        this.amount = expense.getAmount();
        this.createdOn = expense.getCreatedOn();
    }
}
