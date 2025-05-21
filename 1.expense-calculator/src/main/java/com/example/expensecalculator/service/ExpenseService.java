package com.example.expensecalculator.service;

import com.example.expensecalculator.model.Expense;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    private final List<Expense> expenses = new ArrayList<>();

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public double getTotalExpenses() {
        return expenses.stream().mapToDouble(Expense::getAmount).sum();
    }

    public double getAverageDailyExpense() {
        return getTotalExpenses() / 30.0;
    }

    public List<Expense> getTop3Expenses() {
        return expenses.stream()
                .sorted(Comparator.comparingDouble(Expense::getAmount).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    public void clearExpenses() {
        expenses.clear();
    }
} 