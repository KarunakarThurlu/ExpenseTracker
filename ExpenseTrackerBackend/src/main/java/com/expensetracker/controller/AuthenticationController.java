package com.expensetracker.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.expensetracker.dto.SignInDTO;
import com.expensetracker.dto.UserDTO;
import com.expensetracker.entity.Role;
import com.expensetracker.enums.Roles;
import com.expensetracker.jwtsecurity.JwtUtil;
import com.expensetracker.service.UserService;
import com.expensetracker.util.CommonConstants;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@RestController
public class AuthenticationController {
	
	private static Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	private AuthenticationManager authenticationManager;
	
	private JwtUtil jwtUtil;
	
	private UserService userService;
	
	public AuthenticationController(AuthenticationManager authenticationManager,JwtUtil jwtUtil, UserService userService) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		this.userService = userService;
	}
	
	@PostMapping("/signin")
	public ResponseEntity<Map<String, String>> login(@RequestBody UserDTO userDTO) {
		String message = "message";
		Map<String, String> m = new HashMap<>();
		try {
			final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			final String token = jwtUtil.generateToken(authentication);
			m.put("AuthToken", token);
			return new ResponseEntity<>(m, HttpStatus.ACCEPTED);
		} catch (BadCredentialsException e) {
			m.put(message, CommonConstants.INVALID_USERNAME_PASSWORD);
			logger.error("Authentication fails for : {},  message : {}",userDTO.getEmail(),e.getMessage());
			return new ResponseEntity<>(m, HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			m.put(message, e.getMessage());
			return new ResponseEntity<>(m, HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody SignInDTO signinDTO){
		@NotBlank(message = "Email must not be blank")
		@Email(message = "Invalid email format")
		String email = signinDTO.email();
		String name = email.split("@")[0];
		UserDTO userDTO = new  UserDTO();
		userDTO.setFirstName(name);
		userDTO.setLastName(name);
		userDTO.setPhoneNumber("1234567890");
		userDTO.setPassword(signinDTO.password());
		userDTO.setEmail(email);
		userDTO.setRoles(Set.of(new Role(Roles.USER)));
		UserDTO savedUser = userService.saveUser(userDTO);
		if(savedUser.getId()!=null) {
			return ResponseEntity.ok("Signup Completed Successfully");
		}
		return ResponseEntity.internalServerError().body("Signup is UnSuccessful");
	}
}
