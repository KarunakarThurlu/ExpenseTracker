package com.expensetracker.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.expensetracker.dto.UserDTO;
import com.expensetracker.jwtsecurity.JwtUtil;
import com.expensetracker.util.CommonConstants;

@RestController
public class AuthenticationController {
	
	private static Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
	

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping("/login")
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
}
