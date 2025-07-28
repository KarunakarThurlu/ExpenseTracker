package com.expensetracker.util;

import java.util.Set;

import org.apache.catalina.security.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import com.expensetracker.entity.Role;
import com.expensetracker.entity.User;
import com.expensetracker.enums.Roles;
import com.expensetracker.exceptionhandler.CustomGraphQLException;
import com.expensetracker.jwtsecurity.UserDetailsImpl;


public class Utils {
	private static final Logger log = LoggerFactory.getLogger(SecurityUtil.class);
	
	private Utils() {
		// Private constructor to prevent instantiation
	}

	/**
	 * Validates if the current user matches the provided user ID.
	 * 
	 * @param userId The user ID to validate against the current user's ID.
	 * @throws CustomGraphQLException if the current user does not match the provided user ID.
	 */
	public static void validateCurrentUser(Long userId) {
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null || !userDetails.getId().equals(userId)) {
            log.error("User ID mismatch: Current user ID is {}, but provided user ID is {}", userDetails != null ? userDetails.getId() : "null", userId);
            throw new CustomGraphQLException(403, CommonConstants.UNAUTHORIZED_USER);
        }
    }
	
    public static User getCurrentUser(Long userId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null || !userDetails.getId().equals(userId)) {
            log.error("User ID mismatch: Current user ID is {}, but provided user ID is {}", userDetails != null ? userDetails.getId() : "null", userId);
            throw new CustomGraphQLException(403, CommonConstants.UNAUTHORIZED_USER);
        }
        return userDetails.getUser();
    }
    
    public static User getCurrentUser() {
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userDetails == null) {
			log.error("No user is currently authenticated");
			throw new CustomGraphQLException(403, CommonConstants.UNAUTHORIZED_USER);
		}
		return userDetails.getUser();
	}

	public static Long getCurrentUserId() {
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userDetails == null) {
			log.error("No user is currently authenticated");
			throw new CustomGraphQLException(403, CommonConstants.UNAUTHORIZED_USER);
		}
		return userDetails.getId();
	}
	public static boolean isSuperAdmin(Set<Role> set) {
	    return set.stream()
	            .anyMatch(role -> role.getRoleName().equals(Roles.SUPER_ADMIN));
	}
	public static boolean isAdmin(Set<Role> set) {
	    return set.stream()
	            .anyMatch(role -> role.getRoleName().equals(Roles.ADMIN));
	}
}
