package com.example.financialtracker.cron;

import com.example.financialtracker.expense.Expense;
import com.example.financialtracker.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "cron")
public class Cron {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cron_id")
    private long cronId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "expense_id", nullable = false)
    private Expense expense;

    @Column(name = "cron_date", nullable = false)
    private String cronDate;

    public Cron(long userId, long expenseId, String cronDate) {
        User newUser = new User(userId);
        Expense newExpense = new Expense(expenseId);
        this.user = newUser;
        this.expense = newExpense;
        this.cronDate = cronDate;
    }

    @Override
    public String toString() {
        return "Cron{" +
                "cronId=" + cronId +
                ", user=" + user +
                ", expense=" + expense +
                ", cronDate='" + cronDate + '\'' +
                '}';
    }
}
