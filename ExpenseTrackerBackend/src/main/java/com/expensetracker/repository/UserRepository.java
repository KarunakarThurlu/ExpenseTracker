package com.expensetracker.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.expensetracker.entity.User;

public interface UserRepository extends JpaRepository<User,Long> {
	boolean existsByEmail(String email);
	public User findByEmail(String email);
	@Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.email = :email")
	Optional<User> findByEmailWithRoles(@Param("email") String email);
	Page<User> findByTenantId(Long tenantId, Pageable pageable);
	Optional<User> findByIdAndTenantId(Long userId, Long tenantId);
}
