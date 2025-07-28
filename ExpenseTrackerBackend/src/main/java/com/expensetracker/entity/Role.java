package com.expensetracker.entity;

import com.expensetracker.enums.Roles;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name="Roles")
public class Role extends BaseEntity {

	@Enumerated(EnumType.STRING)
	private Roles roleName;

	public Role(Roles roleName) {
		super();
		this.roleName = roleName;
	}

	public Role() {
		super();
	}

	public Roles getRoleName() {
		return roleName;
	}

	public void setRoleName(Roles roleName) {
		this.roleName = roleName;
	}

	@Override
	public String toString() {
		return "{ roleName : " + roleName + " }";
	}
	
	
}
