package com.expensetracker.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "Tenants")
public class Tenant extends BaseEntity{


    @NotBlank(message = "Tenant name is required")
    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "max_users_allowed")
    private int maxUsersAllowed;

    @Column(name = "license_expiry")
    private LocalDate licenseExpiry;

    @OneToMany(mappedBy = "tenant", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<User> users = new HashSet<>();


	public Tenant() {
		super();
	}

	public Tenant(long tenantId) {
		super();
		this.id= tenantId;
	}


	public Tenant(@NotBlank(message = "Tenant name is required") String name, int maxUsersAllowed,
			LocalDate licenseExpiry, Set<User> users) {
		super();
		this.name = name;
		this.maxUsersAllowed = maxUsersAllowed;
		this.licenseExpiry = licenseExpiry;
		this.users = users;
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


	public Set<User> getUsers() {
		return users;
	}


	public void setUsers(Set<User> users) {
		this.users = users;
	}


	@Override
	public String toString() {
		return "{ name=" + name + ", maxUsersAllowed=" + maxUsersAllowed + ", licenseExpiry=" + licenseExpiry
				+ ", users=" + users + " } ";
	}
	
	
	
}
