package com.example.financialtracker.transaction;

import com.example.financialtracker.expense.ExpenseResDto;
import com.example.financialtracker.expense.ExpenseService;
import com.example.financialtracker.income.Income;
import com.example.financialtracker.income.IncomeResDto;
import com.example.financialtracker.income.IncomeService;
import com.example.financialtracker.report.PerYearMonthCat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    private static int compare(TransactionDto transaction1, TransactionDto transaction2) {
        return transaction2.getCreatedOn().compareTo(transaction1.getCreatedOn());
    }

    public List<TransactionDto> getAllTransactions(long userId) {
        List<IncomeResDto> incomes = incomeService.getAllUserIncomes(userId);
        List<ExpenseResDto> expenses = expenseService.getAllUserExpenses(userId, 0, "");

        List<TransactionDto> transactions = new ArrayList<>();

        transactions.addAll(incomes.stream().map(TransactionDto::new).toList());
        transactions.addAll(expenses.stream().map(TransactionDto::new).toList());

        transactions.sort(TransactionService::compare);

        return transactions;
    }
}
