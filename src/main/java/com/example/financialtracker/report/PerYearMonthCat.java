package com.example.financialtracker.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PerYearMonthCat {
    private BigDecimal year;
    private BigDecimal month;
    private long categoryId;
    private BigDecimal total;

    public PerYearMonthCat(Map<String, Object> report ) {
        this.year = (BigDecimal) report.get("year");
        this.month = (BigDecimal) report.get("month");
        this.categoryId = Long.parseLong(String.valueOf(report.get("categoryid")));
        this.total = (BigDecimal) report.get("total");
    }
}
