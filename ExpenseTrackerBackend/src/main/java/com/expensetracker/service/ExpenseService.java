package com.expensetracker.service;

import java.time.LocalDateTime;
import java.util.List;

import com.expensetracker.dto.DashboardData;
import com.expensetracker.dto.ExpenseDTO;
import com.expensetracker.dto.PaginatedResponse;
import com.expensetracker.enums.ExpenseSortField;
import com.expensetracker.enums.SortDirection;

public interface ExpenseService {
	ExpenseDTO saveExpense(ExpenseDTO expenseDTO);
	ExpenseDTO fetchExpense(Long id);
	PaginatedResponse<List<ExpenseDTO>> fetchExpenses(int pageSize, int pageNumber, ExpenseSortField sortBy, SortDirection direction);
	String deleteExpense(Long id);
	ExpenseDTO updateExpense(ExpenseDTO expenseDTO);
	DashboardData dashBoardData(LocalDateTime from , LocalDateTime to);
}
