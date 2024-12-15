package com.expensetracker.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expensetracker.dto.ExpenseDTO;
import com.expensetracker.entity.Expense;
import com.expensetracker.entity.User;
import com.expensetracker.exceptionhandler.CustomGraphQLException;
import com.expensetracker.mappers.ExpenseMapper;
import com.expensetracker.repository.ExpenseRepository;
import com.expensetracker.service.ExpenseService;
import com.expensetracker.util.CommonConstants;

@Service
public class ExpenseServiceImpl implements ExpenseService {
	
	@Autowired
	private ExpenseRepository expenseRepository;

	@Override
	public ExpenseDTO saveExpense(ExpenseDTO expenseDTO) {
	    // Fetch the User by ID (or just set it if you already have the ID)
	    User user = new User(expenseDTO.getUserId());

	    // Map DTO to Entity
	    Expense expense = ExpenseMapper.toExpenseEntity.apply(expenseDTO, user);

	    // Save the Expense entity
	    Expense savedExpense = expenseRepository.save(expense);

	    // Convert saved entity back to DTO
	    return ExpenseMapper.toExpenseDTO.apply(savedExpense);
	}

	@Override
	public ExpenseDTO fetchExpense(Long id) {
		Expense expense = expenseRepository.findById(id)
										   .orElseThrow(()->new CustomGraphQLException(400, CommonConstants.EXPENSE_NOT_FOUND));
		return ExpenseMapper.toExpenseDTO.apply(expense);
	}

	@Override
	public List<ExpenseDTO> fetchExpenses() {
		return expenseRepository.findAll()
				.stream()
				.map(e -> ExpenseMapper.toExpenseDTO.apply(e))
				.toList();
	}

	@Override
	public String deleteExpense(Long id) {
		//Check Expense exists or not
		expenseRepository.findById(id)
						 .orElseThrow(()->new CustomGraphQLException(400, CommonConstants.EXPENSE_NOT_FOUND));
		//Delete Expense
		expenseRepository.deleteById(id);
		return CommonConstants.EXPENSE_DELETE_MSG;
	}

	@Override
	public ExpenseDTO updateExpense(ExpenseDTO expenseDTO) {
		Expense expense = expenseRepository.findById(expenseDTO.getId())
				.orElseThrow(() -> new CustomGraphQLException(400, CommonConstants.EXPENSE_NOT_FOUND));
		Optional.ofNullable(expenseDTO.getDescription()).ifPresent(expense::setDescription);
		Optional.ofNullable(expenseDTO.getAmount()).ifPresent(expense::setAmount);
		Optional.ofNullable(expenseDTO.getCategory()).ifPresent(expense::setCategory);
		Optional.ofNullable(expenseDTO.getDate()).ifPresent(expense::setDate);
		Optional.ofNullable(expenseDTO.getLocation()).ifPresent(expense::setLocation);
		Optional.ofNullable(expenseDTO.getPaymentMethod()).ifPresent(expense::setPaymentMethod);
		Expense updatedExpense = expenseRepository.save(expense);
		return ExpenseMapper.toExpenseDTO.apply(updatedExpense);
	}

}
