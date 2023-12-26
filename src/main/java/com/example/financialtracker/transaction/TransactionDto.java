package com.example.financialtracker.transaction;

import com.example.financialtracker.expense.ExpenseResDto;
import com.example.financialtracker.income.IncomeResDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionDto {
    private long id;

    private long categoryId;

    private String categoryTitle;

    private String title;

    private String description;

    private BigDecimal amount;

    private LocalDateTime createdOn;

    private String type;

    public TransactionDto(ExpenseResDto expenseResDto) {
        this.id = expenseResDto.getId();
        this.categoryId = expenseResDto.getCategoryId();
        this.categoryTitle = expenseResDto.getCategoryTitle();
        this.title = expenseResDto.getExpenseTitle();
        this.description = expenseResDto.getDescription();
        this.amount = expenseResDto.getAmount();
        this.createdOn = expenseResDto.getCreatedOn();
        this.type = "expense";
    }

    public TransactionDto(IncomeResDto incomeResDto) {
        this.id = incomeResDto.getId();
        this.categoryId = incomeResDto.getCategoryId();
        this.categoryTitle = incomeResDto.getCategoryTitle();
        this.title = incomeResDto.getIncomeTitle();
        this.description = incomeResDto.getDescription();
        this.amount = incomeResDto.getAmount();
        this.createdOn = incomeResDto.getCreatedOn();
        this.type = "income";
    }
}
