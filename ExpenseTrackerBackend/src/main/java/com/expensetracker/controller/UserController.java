package com.expensetracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.expensetracker.dto.UserDTO;
import com.expensetracker.service.UserService;

import jakarta.validation.Valid;

@Controller
public class UserController {
	
	
	@Autowired
	private UserService userService;

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

    @QueryMapping
    public UserDTO fetchUser(@Argument("id") Long id) {
        return userService.getUser(id);
    }

    @QueryMapping
    public List<UserDTO> fetchUsers() {
        return userService.getAllUsers();
    }
	
	
}
