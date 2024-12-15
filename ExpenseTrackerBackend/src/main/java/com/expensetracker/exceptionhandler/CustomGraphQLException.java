package com.expensetracker.exceptionhandler;

public class CustomGraphQLException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final int statusCode;
    public CustomGraphQLException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
    public int getStatusCode() {
        return statusCode;
    }

}
