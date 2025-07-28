package com.expensetracker.serviceimpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.expensetracker.dto.DashboardAggregate;
import com.expensetracker.dto.DashboardData;
import com.expensetracker.dto.ExpenseDTO;
import com.expensetracker.dto.KeyValuePair;
import com.expensetracker.dto.PaginatedResponse;
import com.expensetracker.entity.Expense;
import com.expensetracker.entity.User;
import com.expensetracker.enums.Category;
import com.expensetracker.enums.ExpenseSortField;
import com.expensetracker.enums.PaymentMethod;
import com.expensetracker.enums.SortDirection;
import com.expensetracker.enums.TransactionType;
import com.expensetracker.exceptionhandler.CustomGraphQLException;
import com.expensetracker.mappers.ExpenseMapper;
import com.expensetracker.repository.ExpenseRepository;
import com.expensetracker.service.ExpenseService;
import com.expensetracker.util.CommonConstants;
import com.expensetracker.util.QueryConstants;
import com.expensetracker.util.Utils;



@Service
public class ExpenseServiceImpl implements ExpenseService {
	
	private static final Logger log= LoggerFactory.getLogger(ExpenseServiceImpl.class);
	
	private ExpenseRepository expenseRepository;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public ExpenseServiceImpl(ExpenseRepository expenseRepository, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.expenseRepository=expenseRepository;
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}
	
	static Function<Expense, BigDecimal> amount = Expense::getAmount;
	

	@Override
	public ExpenseDTO saveExpense(ExpenseDTO expenseDTO) {
		
		log.info("Saving expense for user with ID: {}", expenseDTO.getUserId());

		Utils.validateCurrentUser(expenseDTO.getUserId());

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
		log.info("Fetching expense with ID: {}", id);
		User currentUser = Utils.getCurrentUser();

		Expense expense = expenseRepository.findById(id)
				.filter(e -> e.getUser().getId().equals(currentUser.getId()))
				.orElseThrow(() -> new CustomGraphQLException(400, CommonConstants.EXPENSE_NOT_FOUND));

		return ExpenseMapper.toExpenseDTO.apply(expense);
	}

	@Override
	public PaginatedResponse<List<ExpenseDTO>> fetchExpenses(int pageSize, int pageNumber, ExpenseSortField sortBy,
			SortDirection direction) {

		log.info("Fetching expenses with pageSize: {}, pageNumber: {}, sortBy: {}, sortDirection: {}", pageSize,
				pageNumber, sortBy, direction);
		User currentUser = Utils.getCurrentUser();
		log.info("Current user ID: {}", currentUser.getId());

		Sort sort = Sort.by(direction == SortDirection.ASC ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy.name());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		log.info("Fetching expenses for user ID: {}", currentUser.getId());
		Page<Expense> page = null;
		
		if (Utils.isSuperAdmin(currentUser.getRoles())) {
			page = expenseRepository.findAll(pageable);
		} else {
			page = expenseRepository.findByUserId(currentUser.getId(), pageable);
		}

		List<Expense> expenses = page.getContent();
		long totalCount = page.getTotalElements();
		List<ExpenseDTO> list = expenses.stream().map(ExpenseMapper.toExpenseDTO::apply).toList();
		return new PaginatedResponse<>(list, totalCount);
	}

	@Override
	public String deleteExpense(Long id) {
		
		//Log the deletion attempt
		log.info("Attempting to delete expense with ID: {}", id);
		User currentUser = Utils.getCurrentUser();
		log.info("Current user ID: {}", currentUser.getId());
		//Check if the expense belongs to the current user
		expenseRepository.findById(id)
				.filter(e -> e.getUser().getId().equals(currentUser.getId()))
				.orElseThrow(() -> new CustomGraphQLException(400, CommonConstants.EXPENSE_NOT_FOUND));
	
		//Delete Expense
		expenseRepository.deleteById(id);
		return CommonConstants.EXPENSE_DELETE_MSG;
	}

	@Override
	public ExpenseDTO updateExpense(ExpenseDTO expenseDTO) {
		// Log the update attempt
		log.info("Attempting to update expense with ID: {}", expenseDTO.getId());
		// Validate the current user
		Utils.validateCurrentUser(expenseDTO.getUserId());
		
		Expense expense = expenseRepository.findById(expenseDTO.getId())
				.orElseThrow(() -> new CustomGraphQLException(400, CommonConstants.EXPENSE_NOT_FOUND));
		if(expenseDTO.getUserId() != null && !expense.getUser().getId().equals(expenseDTO.getUserId())) {
			throw new CustomGraphQLException(403, "You cannot update an expense that does not belong to you.");
		}
		Optional.ofNullable(expenseDTO.getDescription()).ifPresent(expense::setDescription);
		Optional.ofNullable(expenseDTO.getAmount()).ifPresent(expense::setAmount);
		Optional.ofNullable(expenseDTO.getCategory()).ifPresent(expense::setCategory);
		Optional.ofNullable(expenseDTO.getDate()).ifPresent(expense::setDate);
		Optional.ofNullable(expenseDTO.getLocation()).ifPresent(expense::setLocation);
		Optional.ofNullable(expenseDTO.getPaymentMethod()).ifPresent(expense::setPaymentMethod);
		Optional.ofNullable(expenseDTO.getTransactionType()).ifPresent(expense::setTransactionType);
		Expense updatedExpense = expenseRepository.save(expense);
		return ExpenseMapper.toExpenseDTO.apply(updatedExpense);
	}


	@Override
	public DashboardData dashBoardData(LocalDateTime from, LocalDateTime to)  {
		List<KeyValuePair> transactionTypeByAmount = new ArrayList<>();
	    List<KeyValuePair> groupingByCategory = new ArrayList<>();
	    List<KeyValuePair> groupingByPaymentMethod = new ArrayList<>();
	    
	    MapSqlParameterSource params = new MapSqlParameterSource();
	    params.addValue("from", from);
	    params.addValue("to", to);

	    List<DashboardAggregate> rows = namedParameterJdbcTemplate.query(QueryConstants.EXPENSES_QUERY_FOR_DASHBOARD, params,
	        (rs, rowNum) -> {
	            DashboardAggregate agg = new DashboardAggregate();
	            agg.setType(rs.getString("type"));
	            agg.setKey(rs.getString("key"));
	            agg.setValue(rs.getString("value"));
	            return agg;
	        }
	    );
	    for (DashboardAggregate agg : rows) {
	        KeyValuePair kv = new KeyValuePair(agg.getKey(), agg.getValue());
	        switch (agg.getType()) {
	            case "TRANSACTION_TYPE" -> transactionTypeByAmount.add(kv);
	            case "CATEGORY" -> groupingByCategory.add(kv);
	            case "PAYMENT_METHOD" -> groupingByPaymentMethod.add(kv);
	        }
	    }
	    
	    MapSqlParameterSource userId = new MapSqlParameterSource();
	    
		User currentUser = Utils.getCurrentUser();
		log.info("Current SQL: {}", QueryConstants.LAST_FIVE_TRANSACTIONS);
		userId.addValue("userID", currentUser.getId());
	    List<ExpenseDTO> lastFiveTransactions = namedParameterJdbcTemplate.query(QueryConstants.LAST_FIVE_TRANSACTIONS, userId, (rs,rowNum)->{
	    	ExpenseDTO e = new ExpenseDTO();
	    	e.setAmount(rs.getBigDecimal("amount"));
	    	e.setCategory(Category.valueOf(rs.getString("category")));
	    	e.setPaymentMethod(PaymentMethod.valueOf(rs.getString("payment_method")));
	    	e.setTransactionType(TransactionType.valueOf(rs.getString("transaction_Type")));
	    	e.setDate(rs.getObject("date",LocalDateTime.class));
	    	return e;
	    });
	    
	    return new DashboardData(transactionTypeByAmount, groupingByCategory, groupingByPaymentMethod,lastFiveTransactions);
	}

}
