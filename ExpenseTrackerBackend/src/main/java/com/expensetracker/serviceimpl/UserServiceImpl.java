package com.expensetracker.serviceimpl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.expensetracker.dto.PaginatedResponse;
import com.expensetracker.dto.UserDTO;
import com.expensetracker.entity.Role;
import com.expensetracker.entity.User;
import com.expensetracker.exceptionhandler.CustomGraphQLException;
import com.expensetracker.mappers.UserMapper;
import com.expensetracker.repository.RoleRepository;
import com.expensetracker.repository.UserRepository;
import com.expensetracker.service.UserService;
import com.expensetracker.util.CommonConstants;


@Service
public class UserServiceImpl implements UserService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private  UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDTO saveUser(UserDTO userDTO) {
		if (userRepository.existsByEmail(userDTO.getEmail())) {
			throw new CustomGraphQLException(400, "User " + userDTO.getEmail() + " Already Exists");
		}
		User user = UserMapper.toUserEntity.apply(userDTO);
		user.setRoles(saveUserRoles.apply(user.getRoles()));
		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		User savedUser = userRepository.save(user);
		return UserMapper.toUserDTO.apply(savedUser);
	}

	UnaryOperator<Set<Role>> saveUserRoles = rolesFromUI -> {
		logger.debug("Roles from UI: {}", rolesFromUI);
	    Set<Role> roles = new HashSet<>();
	    
	    for (Role role : rolesFromUI) {
	        if (role.getRoleName() == null) {
	            // Skip roles with null role names to avoid any issues
	            continue;
	        }

	        // Try to find the role by name
	        Optional<Role> savedRole = roleRepository.findByRoleName(role.getRoleName());
	        
	        if (savedRole.isPresent()) {
	            // If the role already exists in the database, add it to the set
	            roles.add(savedRole.get());
	        } else {
	            // If the role does not exist, save it to the database and add it to the set
	            Role newRole = roleRepository.save(role);
	            roles.add(newRole);
	        }
	    }
	    logger.debug("Final roles set: {}", roles);
	    return roles;
	};

	@Override
	public UserDTO updateUser(UserDTO userDTO) {
		User user = userRepository.findById(userDTO.getId())
				.orElseThrow(() -> new CustomGraphQLException(400, CommonConstants.USER_NOT_FOUND));
		Optional.ofNullable(userDTO.getFirstName()).ifPresent(user::setFirstName);
		Optional.ofNullable(userDTO.getLastName()).ifPresent(user::setLastName);
		Optional.ofNullable(userDTO.getGendar()).ifPresent(user::setGendar);
		Optional.ofNullable(userDTO.getPhoneNumber()).ifPresent(user::setPhoneNumber);
		// user.setRoles(saveUserRoles.apply(user.getRoles()));
		User savedUser = userRepository.save(user);
		return UserMapper.toUserDTO.apply(savedUser);
	}

	@Override
	public String deleteUser(Long userId) {
		Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            userRepository.deleteById(userId);
            return "User Deleted Successfully";
        }
        return CommonConstants.USER_NOT_FOUND;
	}

	@Override
	public UserDTO getUser(Long userId) {
		User user = userRepository.findById(userId)
								  .orElseThrow(()->new CustomGraphQLException(400, CommonConstants.USER_NOT_FOUND));
		return UserMapper.toUserDTO.apply(user);
	}

	@Override
	public PaginatedResponse<List<UserDTO>> getAllUsers() {
		List<User> users = userRepository.findAll();
		return new PaginatedResponse<>(users.stream().map(u->UserMapper.toUserDTO.apply(u)).toList(), 10);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

}
