package com.example.financialtracker.report;

import jakarta.persistence.Tuple;
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
public class PerMonthCatExpense {
    private int year;
    private int month;
    private BigDecimal total;
    private long count;

    public PerMonthCatExpense(Map<String, Object> report ) {
        this.year = (int) report.get("year");
        this.month = (int) report.get("month");
        this.total = (BigDecimal) report.get("total");
        this.count = (long) report.get("count");
    }
}