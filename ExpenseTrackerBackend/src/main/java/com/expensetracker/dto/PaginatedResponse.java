package com.expensetracker.dto;

public class PaginatedResponse<T> {
	T data;
	Long total;
	
	public PaginatedResponse(T data, Long total) {
		super();
		this.data = data;
		this.total = total;
	}

	public PaginatedResponse() {
		super();
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "{ data : " + data + ", total : " + total + " } ";
	}	
	
	
}
