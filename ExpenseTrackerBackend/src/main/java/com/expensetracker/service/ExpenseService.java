package com.expensetracker.service;

import java.util.List;

import com.expensetracker.dto.ExpenseDTO;

public interface ExpenseService {
	ExpenseDTO saveExpense(ExpenseDTO expenseDTO);
	ExpenseDTO fetchExpense(Long id);
	List<ExpenseDTO> fetchExpenses();
	String deleteExpense(Long id);
	ExpenseDTO updateExpense(ExpenseDTO expenseDTO);
}
