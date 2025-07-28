package com.expensetracker.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.expensetracker.dto.DashboardData;
import com.expensetracker.dto.ExpenseDTO;
import com.expensetracker.dto.PaginatedResponse;
import com.expensetracker.enums.ExpenseSortField;
import com.expensetracker.enums.SortDirection;
import com.expensetracker.service.ExpenseService;

@Controller
public class ExpenseController {
	
	private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);
	
	@Autowired
	private ExpenseService expenseService;
	
	@MutationMapping
	public ExpenseDTO saveExpense(@Argument("expense") ExpenseDTO expenseDTO) {
		logger.info("Received expenseDTO: {}", expenseDTO);
        return expenseService.saveExpense(expenseDTO);
    }
	
	@MutationMapping
	public ExpenseDTO updateExpense(@Argument("expense") ExpenseDTO expenseDTO) {
		return expenseService.updateExpense(expenseDTO);
	}
	
	@MutationMapping
	public String deleteExpense(@Argument("id") Long id) {
		return expenseService.deleteExpense(id);
	}
	
	@QueryMapping(name="expense")
	public ExpenseDTO fetchExpense(@Argument("id") Long id) {
		return expenseService.fetchExpense(id);
	}
	
	@QueryMapping(name="expenses")
	public PaginatedResponse<List<ExpenseDTO>> fetchExpenses(
			@Argument("pageSize") int pageSize,
			@Argument("pageNumber") int pageNumber, 
			@Argument("sortBy") String sortBy,
			@Argument("sortDirection") String sortDirection){
		PaginatedResponse<List<ExpenseDTO>> fetchExpenses = expenseService.fetchExpenses(pageSize, pageNumber, ExpenseSortField.valueOf(sortBy),
				SortDirection.valueOf(sortDirection));
		logger.info("Returning {} expenses", fetchExpenses);
		return fetchExpenses;
	}
	
	@QueryMapping(name="dashboardData")
	public DashboardData dashboardData(@Argument("fromDate") String  fromDate ,@Argument("toDate") String toDate) {
		LocalDateTime fromDateTime = LocalDateTime.parse(fromDate,DateTimeFormatter.ISO_DATE_TIME);
		LocalDateTime toDateTime = LocalDateTime.parse(toDate,DateTimeFormatter.ISO_DATE_TIME);
		return expenseService.dashBoardData(fromDateTime, toDateTime);
	}

}
