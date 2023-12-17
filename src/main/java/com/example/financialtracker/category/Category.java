package com.example.financialtracker.category;

import com.example.financialtracker.user.User;
import com.example.financialtracker.wrapper.ApiResponse;
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
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private long categoryId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String title;

    private String description;

    @Column(name = "amount_limit")
    private BigDecimal amountLimit;

    @Column(name = "item_limit")
    private int itemLimit;

    @Column(name = "created_on")
    private LocalDate createdOn = LocalDate.now();

    private boolean status = true;

    public Category(CategoryReqDto categoryReqDto, User user) {
        this.user = user;
        this.title = categoryReqDto.getTitle();
        this.description = categoryReqDto.getDescription();
        this.amountLimit = categoryReqDto.getAmountLimit();
        this.itemLimit = categoryReqDto.getItemLimit();
    }
}
