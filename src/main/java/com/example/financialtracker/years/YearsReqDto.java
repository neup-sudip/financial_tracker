package com.example.financialtracker.years;

import jakarta.validation.constraints.DecimalMin;
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

    @DecimalMin(value = "1", message = "Amount Limit must be at least 1.")
    private BigDecimal amountLimit;

    private int itemLimit;
}
