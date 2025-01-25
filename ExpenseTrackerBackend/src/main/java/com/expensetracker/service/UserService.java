package com.expensetracker.service;

import java.util.List;

import com.expensetracker.dto.PaginatedResponse;
import com.expensetracker.dto.UserDTO;
import com.expensetracker.entity.User;

public interface UserService {
	UserDTO saveUser(UserDTO userDTO);
	UserDTO updateUser(UserDTO userDTO);
	String deleteUser(Long userId);
	UserDTO getUser(Long userId);
	PaginatedResponse<List<UserDTO>> getAllUsers();
	User findByEmail(String email);
}
