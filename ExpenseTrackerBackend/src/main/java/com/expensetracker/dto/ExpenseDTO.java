package com.expensetracker.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.expensetracker.enums.Category;
import com.expensetracker.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;

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
    private Category category;
    @NotBlank(message = "Select Payment method")
    private PaymentMethod paymentMethod;
    
    private Long userId;
    
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    
	public ExpenseDTO() {
		super();
	}
	
	public ExpenseDTO(Long id, String description, BigDecimal amount, LocalDateTime date, String location,
			Category category, PaymentMethod paymentMethod, Long userId) {
		super();
		this.id = id;
		this.description = description;
		this.amount = amount;
		this.date = date;
		this.location = location;
		this.category = category;
		this.paymentMethod = paymentMethod;
		this.userId = userId;
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
		return "ExpenseDTO [id=" + id + ", description=" + description + ", amount=" + amount + ", date=" + date
				+ ", location=" + location + ", category=" + category + ", paymentMethod=" + paymentMethod + ", userId="
				+ userId + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
	
    
}

