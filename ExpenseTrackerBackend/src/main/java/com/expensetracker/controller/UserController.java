package com.expensetracker.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.expensetracker.dto.PaginatedResponse;
import com.expensetracker.dto.UserDTO;
import com.expensetracker.service.UserService;

import jakarta.validation.Valid;

@Controller
public class UserController {
	
	private static Logger logger = LoggerFactory.getLogger(UserController.class);
	
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

    @QueryMapping(name="user")
    public UserDTO fetchUser(@Argument("id") Long id) {
        return userService.getUser(id);
    }

    @QueryMapping(name="users")
	public PaginatedResponse<List<UserDTO>> fetchUsers(@Argument("pageSize") Long pageSize, @Argument("pageNumber") Long pageNumber,
			@Argument("fromDate") LocalDateTime fromDate, @Argument("toDate") LocalDateTime toDate) {
    	logger.info("Params pageSize : {}, pageNumber : {}, from : {}, to : {} ", pageSize,pageNumber,fromDate,toDate);
    	return userService.getAllUsers();
    }
	
	
}
