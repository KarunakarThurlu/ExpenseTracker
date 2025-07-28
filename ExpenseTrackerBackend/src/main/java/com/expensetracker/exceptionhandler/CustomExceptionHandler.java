package com.expensetracker.exceptionhandler;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.expensetracker.util.CommonConstants;

import io.jsonwebtoken.ExpiredJwtException;

@ControllerAdvice
public class CustomExceptionHandler{
	
    @ExceptionHandler(CustomGraphQLException.class)
    public ResponseEntity<Map<String,Object>> handleResourceNotFoundException(CustomGraphQLException ex, WebRequest request) {
        Map<String,Object> errorDetails = Map.of(CommonConstants.RESPONSE_MESSAGE,ex.getMessage(),
        						  "errorCode",ex.getStatusCode());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Map<String,Object>> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
        Map<String,Object> errorDetails = Map.of(CommonConstants.RESPONSE_MESSAGE,ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
		Map<String, Object> errorDetails = Map.of(CommonConstants.RESPONSE_MESSAGE, ex.getMessage());
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}
}
