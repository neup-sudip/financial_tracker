package com.example.financialtracker.years;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class YearsResDto {
    private long id;

    private int year;

    private BigDecimal amountLimit;

    private int itemLimit;

    public YearsResDto(Years years) {
        this.id = years.getId();
        this.year = years.getYear();
        this.amountLimit = years.getAmountLimit();
        this.itemLimit = years.getItemLimit();
    }
}
