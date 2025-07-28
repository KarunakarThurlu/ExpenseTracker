package com.expensetracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for changing user password.
 * 
 * This record holds the email and new password for a user who wants to change their password.
 * It includes validation constraints to ensure that the email is in a valid format and that the password meets minimum length requirements.
 */
public record ChangePasswordDTO(
		@NotBlank(message = "Email must not be blank") 
		@Email(message = "Invalid email format") 
		String email,

		@NotBlank(message = "Password must not be blank") 
		@Size(min = 5, message = "Password must be at least 5 characters long") 
		String password,
		
		String oldPassword
		) {
}
