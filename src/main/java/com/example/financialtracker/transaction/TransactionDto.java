package com.example.financialtracker.transaction;

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
public class TransactionDto {
    private String recordType;
    private String categoryTitle;
    private String title;
    private String description;
    private BigDecimal amount;
    private LocalDateTime createdOn;
}
