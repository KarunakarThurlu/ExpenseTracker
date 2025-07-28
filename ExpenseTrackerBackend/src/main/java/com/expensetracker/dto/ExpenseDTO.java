package com.expensetracker.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.expensetracker.enums.Category;
import com.expensetracker.enums.PaymentMethod;
import com.expensetracker.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ExpenseDTO {
    
	private Long id;
	
    private String description;
    
    @NotBlank(message = "Enter amount")
    private BigDecimal amount;
    
    @NotBlank(message = "Mention date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime date;
    
    private String location;
    
    @NotBlank(message = "Select Category date")
    @Enumerated(EnumType.STRING)
    private Category category;
    
    @NotBlank(message = "Select Payment method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    
    @NotBlank(message = "Select Transaction Type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    
    @NotBlank(message = "User ID is required")
    @NotNull(message = "User ID cannot be null")
    private Long userId;
    
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

	public ExpenseDTO() {
		super();
	}

	public ExpenseDTO(Long id, String description, @NotBlank(message = "Enter amount") BigDecimal amount,
			@NotBlank(message = "Mention date") LocalDateTime date, String location,
			@NotBlank(message = "Select Category date") Category category,
			@NotBlank(message = "Select Payment method") PaymentMethod paymentMethod,
			@NotBlank(message = "Select Transaction Type") TransactionType transactionType, Long userId,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.description = description;
		this.amount = amount;
		this.date = date;
		this.location = location;
		this.category = category;
		this.paymentMethod = paymentMethod;
		this.transactionType = transactionType;
		this.userId = userId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return " { id=" + id + ", description=" + description + ", amount=" + amount + ", date=" + date
				+ ", location=" + location + ", category=" + category + ", paymentMethod=" + paymentMethod
				+ ", transactionType=" + transactionType + ", userId=" + userId + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + " } ";
	}
    
    
    
}

