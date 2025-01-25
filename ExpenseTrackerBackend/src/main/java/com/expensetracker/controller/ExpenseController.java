package com.expensetracker.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.expensetracker.dto.ExpenseDTO;
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
	public List<ExpenseDTO> fetchExpenses(){
		return expenseService.fetchExpenses();
	}

}
