package com.example.financialtracker.expense;

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
public class ExpenseReqDto {
    @Positive(message = "Invalid category !")
    private long categoryId;

    @NotEmpty(message = "Title is required !")
    private String title;

    @NotEmpty(message = "Description is required !")
    private String description;

    @DecimalMin(value = "1", message = "Amount Limit must be at least 1.")
    private BigDecimal amount;

    @NotNull(message = "Date is required")
    @Past(message = "Date must be in the past")
    private LocalDateTime date;

    private String cronFor;
}
