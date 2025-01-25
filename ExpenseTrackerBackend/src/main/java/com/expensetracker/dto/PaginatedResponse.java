package com.expensetracker.dto;

public class PaginatedResponse<T> {
	T data;
	Integer total;
	
	public PaginatedResponse() {
		super();
	}

	public PaginatedResponse(T data, Integer total) {
		super();
		this.data = data;
		this.total = total;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "{ data : " + data + ", total : " + total + " }";
	}	
}
