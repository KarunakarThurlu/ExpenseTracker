package com.expensetracker.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.expensetracker.enums.Category;
import com.expensetracker.enums.PaymentMethod;
import com.expensetracker.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Expenses")
public class Expense extends BaseEntity {
	
	private String description;
    private BigDecimal amount;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime date;
    private String location;
    
    @Enumerated(EnumType.STRING)
    private Category category;
    
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

	public Expense() {
		super();
	}

	public Expense(String description, BigDecimal amount, LocalDateTime date, String location, Category category,
			PaymentMethod paymentMethod, TransactionType transactionType, User user) {
		super();
		this.description = description;
		this.amount = amount;
		this.date = date;
		this.location = location;
		this.category = category;
		this.paymentMethod = paymentMethod;
		this.transactionType = transactionType;
		this.user = user;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return " { description=" + description + ", amount=" + amount + ", date=" + date + ", location="
				+ location + ", category=" + category + ", paymentMethod=" + paymentMethod + ", transactionType="
				+ transactionType + ", user=" + user + " } ";
	}
    
    
    
	
}
