package com.expensetracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignInDTO(
		@NotBlank(message = "Email must not be blank") 
		@Email(message = "Invalid email format") 
		String email,

		@NotBlank(message = "Password must not be blank") 
		@Size(min = 5, message = "Password must be at least 5 characters long") 
		String password) {
}
