package com.example.financialtracker.incomecategory;

import com.example.financialtracker.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "income_category")
public class IncomeCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private long categoryId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String title;

    private String description;

    @Column(name = "created_on")
    private LocalDateTime createdOn = LocalDateTime.now();

    private boolean status = true;

    public IncomeCategory(IncomeCategoryReqDto incomeCategoryReqDto, User user) {
        this.user = user;
        this.title = incomeCategoryReqDto.getTitle();
        this.description = incomeCategoryReqDto.getDescription();
    }
}
