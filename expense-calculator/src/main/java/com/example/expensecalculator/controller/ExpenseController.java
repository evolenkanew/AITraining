package com.example.expensecalculator.controller;

import com.example.expensecalculator.model.Expense;
import com.example.expensecalculator.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public void addExpense(@RequestBody Expense expense) {
        expenseService.addExpense(expense);
    }

    @GetMapping
    public List<Expense> getExpenses() {
        return expenseService.getExpenses();
    }

    @GetMapping("/summary")
    public Map<String, Object> getSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("total", expenseService.getTotalExpenses());
        summary.put("averageDaily", expenseService.getAverageDailyExpense());
        summary.put("top3", expenseService.getTop3Expenses());
        return summary;
    }

    @DeleteMapping
    public void clearExpenses() {
        expenseService.clearExpenses();
    }
} 