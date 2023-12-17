package com.example.financialtracker.expense;

import com.example.financialtracker.category.Category;
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
    private Category category;

    private String title;

    private String description;

    private BigDecimal amount;

    @Column(name = "created_on")
    private LocalDate createdOn = LocalDate.now();

    public Expense(ExpenseReqDto expenseReqDto, User user) {
        this.user = user;
        Category newCat = new Category();
        newCat.setCategoryId(expenseReqDto.getCategoryId());
        this.category = newCat;
        this.title  = expenseReqDto.getTitle();
        this.description = expenseReqDto.getDescription();
        this.amount = expenseReqDto.getAmount();
    }
}
