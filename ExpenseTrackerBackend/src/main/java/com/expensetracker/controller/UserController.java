package com.expensetracker.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import com.expensetracker.dto.ChangePasswordDTO;
import com.expensetracker.dto.PaginatedResponse;
import com.expensetracker.dto.UserDTO;
import com.expensetracker.enums.SortDirection;
import com.expensetracker.enums.UserSortField;
import com.expensetracker.service.UserService;

import jakarta.validation.Valid;

@Controller
public class UserController {

	private static Logger logger = LoggerFactory.getLogger(UserController.class);

	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService  = userService;
	}

	@MutationMapping
	public UserDTO saveUser(@Valid @Argument("user") UserDTO userDTO) {
		return userService.saveUser(userDTO);
	}

	@MutationMapping
	public UserDTO updateUser(@Valid @Argument("user") UserDTO userDTO) {
		return userService.updateUser(userDTO);
	}

	@MutationMapping
	public String deleteUser(@Argument("id") Long id) {
		return userService.deleteUser(id);
	}

	@QueryMapping(name = "user")
	public UserDTO fetchUser(@Argument("id") Long id) {
		return userService.getUser(id);
	}

	@QueryMapping(name = "users")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
	public PaginatedResponse<List<UserDTO>> fetchUsers(
			@Argument("pageSize") int pageSize,
			@Argument("pageNumber") int pageNumber,
			@Argument("sortBy") String sortBy,
			@Argument("sortDirection") String sortDirection) {
		logger.info("Params pageSize : {}, pageNumber : {}, sortBy : {}, sortDirection : {} ", pageSize, pageNumber, sortBy, sortDirection);
		return userService.getAllUsers(pageSize, pageNumber, UserSortField.valueOf(sortBy), SortDirection.valueOf(sortDirection));
	}

	@MutationMapping(name = "updatePassword")
	public String updatePassword(@Argument("updatePassword") ChangePasswordDTO changePasswordDTO) {
		return userService.updatePassword(changePasswordDTO);
	}

}
