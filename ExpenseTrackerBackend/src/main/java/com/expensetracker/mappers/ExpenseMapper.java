package com.expensetracker.mappers;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.expensetracker.dto.ExpenseDTO;
import com.expensetracker.entity.Expense;
import com.expensetracker.entity.User;

public class ExpenseMapper {

    public static final Function<Expense, ExpenseDTO> toExpenseDTO = expense -> {
        ExpenseDTO dto = new ExpenseDTO();
        dto.setCreatedAt(expense.getCreatedAt());
        dto.setUpdatedAt(expense.getUpdatedAt());
        dto.setId(expense.getId());
        dto.setDescription(expense.getDescription());
        dto.setAmount(expense.getAmount());
        dto.setDate(expense.getDate());
        dto.setLocation(expense.getLocation());
        dto.setCategory(expense.getCategory());
        dto.setPaymentMethod(expense.getPaymentMethod());
        
        // Map user information (if needed, could also map just the user ID)
        if (expense.getUser() != null) {
            dto.setUserId(expense.getUser().getId()); // Assuming ExpenseDTO has a userId field
        }
        return dto;
    };

    public static final BiFunction<ExpenseDTO,User, Expense> toExpenseEntity = (expenseDTO,user) -> {
        Expense expense = new Expense();
        expense.setDescription(expenseDTO.getDescription());
        expense.setAmount(expenseDTO.getAmount());
        expense.setDate(expenseDTO.getDate());
        expense.setLocation(expenseDTO.getLocation());
        expense.setCategory(expenseDTO.getCategory());
        expense.setPaymentMethod(expenseDTO.getPaymentMethod());
        expense.setUser(user);
        return expense;
    };
}
