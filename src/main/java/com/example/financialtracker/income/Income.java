package com.example.financialtracker.income;

import com.example.financialtracker.incomecategory.IncomeCategory;
import com.example.financialtracker.user.User;
import jakarta.persistence.*;
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
@Entity
@Table(name = "income")
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "income_id")
    private long incomeId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private IncomeCategory incomeCategory;

    private String title;

    private String description;

    private BigDecimal amount;

    @Column(name = "created_on")
    private LocalDate createdOn;

    public Income(IncomeReqDto incomeReqDto, User user) {
        this.user = user;
        IncomeCategory newCat = new IncomeCategory();
        newCat.setCategoryId(incomeReqDto.getCategoryId());
        this.incomeCategory = newCat;
        this.title  = incomeReqDto.getTitle();
        this.description = incomeReqDto.getDescription();
        this.amount = incomeReqDto.getAmount();
        this.createdOn = incomeReqDto.getDate();
    }
}

