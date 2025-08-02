package com.expensetracker.entity;

import java.util.List;
import java.util.Set;

import com.expensetracker.enums.Gendar;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="Users")
public class User extends BaseEntity {

    @NotBlank(message = "First name is mandatory")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 5, message = "Password must be at least 8 characters long")
    private String password;

    @Pattern(regexp = "^\\+?[0-9]{10}$", message = "Phone number should be valid and between 10 to 15 digits")
    private String phoneNumber;
	
	@Enumerated(EnumType.STRING)
	private Gendar gendar;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "User_Roles", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
	private Set<Role> roles;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Expense> expenses;

    @ManyToOne(optional = false, fetch = FetchType.LAZY) // optional = false => not nullable
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by_id")
	private User createdBy;
	
	
	// Default Constructor
	public User() {
		super();
	}
	
	public User(long userId) {
		super();
		this.id= userId;
	}
	
	//Parameterized Constructor
	public User(String firstName, String lastName, String email, String password, String phoneNumber, Gendar gendar,
			Set<Role> roles, List<Expense> expenses, Tenant tenant, User createdBy) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.gendar = gendar;
		this.roles = roles;
		this.expenses = expenses;
		this.tenant = tenant;
		this.createdBy = createdBy;
	}
	
	// Getters and Setters all fields
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
	public List<Expense> getExpenses() {
		return expenses;
	}
	public void setExpenses(List<Expense> expenses) {
		this.expenses = expenses;
	}
	public Tenant getTenant() {
		return tenant;
	}
	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}
	public User getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}
	
	@Override
	public String toString() {
		return "{ firstName : " + firstName + ", lastName : " + lastName + ", email : " + email + ", phoneNumber : "
				+ phoneNumber + ", gendar : " + gendar + ", roles : " + roles + ", expenses : " + expenses
				+ ", tenant : " + tenant + ", createdBy : " + createdBy + " }";
	}
			
}
