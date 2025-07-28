package com.expensetracker.service;

import java.util.List;

import com.expensetracker.dto.ChangePasswordDTO;
import com.expensetracker.dto.PaginatedResponse;
import com.expensetracker.dto.UserDTO;
import com.expensetracker.entity.User;
import com.expensetracker.enums.SortDirection;
import com.expensetracker.enums.UserSortField;

public interface UserService {
	UserDTO saveUser(UserDTO userDTO);
	UserDTO updateUser(UserDTO userDTO);
	String deleteUser(Long userId);
	UserDTO getUser(Long userId);
	PaginatedResponse<List<UserDTO>> getAllUsers(int pageSize, int pageNumber, UserSortField sortBy, SortDirection direction);
	User findByEmail(String email);
	String updatePassword(ChangePasswordDTO changePasswordDTO);
	User findByEmailWithRoles(String email);
}
