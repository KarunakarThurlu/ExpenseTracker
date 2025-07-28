package com.expensetracker.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import com.expensetracker.entity.User;

public class TenantDTO {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
    private String name;

    private int maxUsersAllowed;

    private LocalDate licenseExpiry;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;

    private Set<User> users;

    
	public TenantDTO() {
		super();
	}

	public TenantDTO(Long id, String name, int maxUsersAllowed, LocalDate licenseExpiry, LocalDateTime createdAt,LocalDateTime updatedAt, Set<User> users) {
		super();
		this.id = id;
		this.name = name;
		this.maxUsersAllowed = maxUsersAllowed;
		this.licenseExpiry = licenseExpiry;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.users = users;
	}
	
	


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMaxUsersAllowed() {
		return maxUsersAllowed;
	}

	public void setMaxUsersAllowed(int maxUsersAllowed) {
		this.maxUsersAllowed = maxUsersAllowed;
	}

	public LocalDate getLicenseExpiry() {
		return licenseExpiry;
	}

	public void setLicenseExpiry(LocalDate licenseExpiry) {
		this.licenseExpiry = licenseExpiry;
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

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "TenantDTO [id=" + id + ", name=" + name + ", maxUsersAllowed=" + maxUsersAllowed + ", licenseExpiry="
				+ licenseExpiry + ", users=" + users + "]";
	}
	
    
}
