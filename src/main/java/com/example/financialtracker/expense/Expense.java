package com.example.financialtracker.expense;

import com.example.financialtracker.expensecategory.ExpenseCategory;
import com.example.financialtracker.user.User;
import jakarta.persistence.*;
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
@Entity
@Table(name = "expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id")
    private long expenseId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private ExpenseCategory expenseCategory;

    private String title;

    private String description;

    private BigDecimal amount;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    public Expense(ExpenseReqDto expenseReqDto, User user) {
        this.user = user;
        this.expenseCategory = new ExpenseCategory(expenseReqDto.getCategoryId());
        this.title  = expenseReqDto.getTitle();
        this.description = expenseReqDto.getDescription();
        this.amount = expenseReqDto.getAmount();
        this.createdOn = expenseReqDto.getDate();
    }

    public Expense(long expenseId){
        this.expenseId = expenseId;
    }

}
