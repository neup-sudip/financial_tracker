package com.example.financialtracker.years;

import com.example.financialtracker.expensecategory.ExpenseCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Year;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "years")
public class Years {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private ExpenseCategory expenseCategory;

    @Column(name = "year")
    private int year;

    @Column(name = "amount_limit")
    private BigDecimal amountLimit;

    @Column(name = "item_limit")
    private int itemLimit;

    public Years(YearsReqDto yearsReqDto,long categoryId) {
        ExpenseCategory expenseCategory1 = new ExpenseCategory();
        expenseCategory1.setCategoryId(categoryId);
        this.expenseCategory = expenseCategory1;
        this.year = yearsReqDto.getYear();
        this.amountLimit = yearsReqDto.getAmountLimit();
        this.itemLimit = yearsReqDto.getItemLimit();
    }
}
