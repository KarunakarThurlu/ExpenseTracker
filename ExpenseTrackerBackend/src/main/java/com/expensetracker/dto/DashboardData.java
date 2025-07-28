package com.expensetracker.dto;

import java.util.List;

public class DashboardData {
    private List<KeyValuePair> transactionTypeByAmount;
    private List<KeyValuePair> groupingByCategory;
    private List<KeyValuePair> groupingByPaymentMethod;
    private List<ExpenseDTO> lastFiveTransactions;
    public DashboardData(
        List<KeyValuePair> transactionTypeByAmount,
        List<KeyValuePair> groupingByCategory,
        List<KeyValuePair> groupingByPaymentMethod,
        List<ExpenseDTO> lastFiveTransactions
    ) {
        this.transactionTypeByAmount = transactionTypeByAmount;
        this.groupingByCategory = groupingByCategory;
        this.groupingByPaymentMethod = groupingByPaymentMethod;
        this.lastFiveTransactions= lastFiveTransactions;
    }

    // Getters
    public List<KeyValuePair> getTransactionTypeByAmount() {
        return transactionTypeByAmount;
    }

    public List<KeyValuePair> getGroupingByCategory() {
        return groupingByCategory;
    }

    public List<KeyValuePair> getGroupingByPaymentMethod() {
        return groupingByPaymentMethod;
    }

	public List<ExpenseDTO> getLastFiveTaansaction() {
		return lastFiveTransactions;
	}
    
}
