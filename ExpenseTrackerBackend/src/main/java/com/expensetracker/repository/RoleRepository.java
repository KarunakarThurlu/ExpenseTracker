package com.expensetracker.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expensetracker.entity.Role;
import com.expensetracker.enums.Roles;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByRoleName(Roles roleName);
	List<Role> findByRoleNameIn(Set<Roles> roleNames);
}
