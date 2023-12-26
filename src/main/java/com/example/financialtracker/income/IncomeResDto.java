package com.example.financialtracker.income;

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
public class IncomeResDto {
    private long id;

    private long categoryId;

    private String categoryTitle;

    private String incomeTitle;

    private String description;

    private BigDecimal amount;

    private LocalDateTime createdOn;

    public IncomeResDto(Income income) {
        this.id = income.getIncomeId();
        this.categoryId = income.getIncomeCategory().getCategoryId();
        this.categoryTitle = income.getIncomeCategory().getTitle();
        this.incomeTitle = income.getTitle();
        this.description = income.getDescription();
        this.amount = income.getAmount();
        this.createdOn = income.getCreatedOn();
    }
}
