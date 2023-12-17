package com.example.financialtracker.category;

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
public class CategoryResDto {
    private long categoryId;

    private String title;

    private String description;

    private BigDecimal amountLimit;

    private int itemLimit;

    private LocalDate createdOn;

    private boolean status;

    public CategoryResDto(Category category) {
        this.categoryId = category.getCategoryId();
        this.title = category.getTitle();
        this.description = category.getDescription();
        this.amountLimit = category.getAmountLimit();
        this.itemLimit= category.getItemLimit();
        this.createdOn = category.getCreatedOn();
        this.status = category.isStatus();
    }
}
