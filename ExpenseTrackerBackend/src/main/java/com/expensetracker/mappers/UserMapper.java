package com.expensetracker.mappers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.expensetracker.dto.ExpenseDTO;
import com.expensetracker.dto.UserDTO;
import com.expensetracker.entity.User;

public class UserMapper {

    public static final Function<User, UserDTO> toUserDTO = user -> {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setGendar(user.getGendar());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
     // Explicitly specify the type for roles, assuming it's Set<String>
        dto.setRoles(user.getRoles());
        
        // Map Expenses to ExpenseDTO
        List<ExpenseDTO> expenseDTOs = Optional.ofNullable(user.getExpenses())
        	    .orElse(Collections.emptyList()) // If expenses is null, use an empty list
        	    .stream()
        	    .map(ExpenseMapper.toExpenseDTO)
        	    .collect(Collectors.toList());
        dto.setExpenses(expenseDTOs);

        return dto;
    };

    public static final Function<UserDTO, User> toUserEntity = userDTO -> {
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setGendar(userDTO.getGendar());
        user.setPassword(userDTO.getPassword());
        user.setRoles(userDTO.getRoles());

        // Map expenses from ExpenseDTO back to Expense entity
//        List<Expense> expenses = Optional.ofNullable(userDTO.getExpenses())
//        	    .orElse(Collections.emptyList()) // If expenses is null, use an empty list
//        	    .stream()
//        	    .map(ExpenseMapper.toExpenseEntity)
//        	    .collect(Collectors.toList());
//        user.setExpenses(expenses);

        return user;
    };
}
