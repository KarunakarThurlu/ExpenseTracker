package com.expensetracker.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.expensetracker.entity.Role;
import com.expensetracker.enums.Gendar;
import com.expensetracker.enums.Roles;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserDTO {
	
	private Long id;

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    private String password;

    @Pattern(regexp = "^\\+?[0-9. ()-]{10}$", message = "Phone number should be valid and between 10 to 15 digits")
    private String phoneNumber;
    
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Gendar gendar;
    private Set<Role> roles;
    private List<ExpenseDTO> expenses;
    
    
	public UserDTO() {
		super();
	}

	public UserDTO(Long id, @NotBlank(message = "First name is mandatory") String firstName,
			@NotBlank(message = "Last name is mandatory") String lastName,
			@NotBlank(message = "Email is mandatory") @Email(message = "Email should be valid") String email,
			@NotBlank(message = "Password is mandatory") @Size(min = 8, message = "Password must be at least 8 characters long") String password,
			@Pattern(regexp = "^\\+?[0-9. ()-]{10}$", message = "Phone number should be valid and between 10 to 15 digits") String phoneNumber,
			LocalDateTime createdAt, LocalDateTime updatedAt, Gendar gendar, Set<Role> roles,
			List<ExpenseDTO> expenses) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.gendar = gendar;
		this.roles = roles;
		this.expenses = expenses;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public Gendar getGendar() {
		return gendar;
	}


	public void setGendar(Gendar gendar) {
		this.gendar = gendar;
	}


	public Set<Role> getRoles() {
		return roles;
	}


	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}


	public List<ExpenseDTO> getExpenses() {
		return expenses;
	}


	public void setExpenses(List<ExpenseDTO> expenses) {
		this.expenses = expenses;
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
  
}
