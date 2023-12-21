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
public class YearsReqDto {
    private int year;

    private BigDecimal amountLimit;

    private int itemLimit;
}
