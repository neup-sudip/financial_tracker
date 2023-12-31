package com.example.financialtracker.expensepending;

import com.example.financialtracker.cron.Cron;
import com.example.financialtracker.expense.Expense;
import com.example.financialtracker.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
@Table(name = "pending_expense")
public class PendingExpense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pending_id")
    private long pendingId;

    @Column(name = "category_id")
    private long categoryId;

    private String title;

    private String description;

    private BigDecimal amount;

    private LocalDateTime date;

    @Column(name = "user_id")
    private long userId;

    public PendingExpense(Cron cron) {
        Expense expense = cron.getExpense();
        this.categoryId = expense.getExpenseCategory().getCategoryId();
        this.title = expense.getTitle();
        this.description = expense.getDescription();
        this.amount = expense.getAmount();
        this.date = LocalDateTime.now();
        this.userId = cron.getUser().getUserId();
    }
}
