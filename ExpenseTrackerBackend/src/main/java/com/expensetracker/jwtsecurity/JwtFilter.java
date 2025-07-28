package com.expensetracker.jwtsecurity;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.expensetracker.exceptionhandler.CustomGraphQLException;
import com.expensetracker.util.CommonConstants;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	private final Logger log = LoggerFactory.getLogger(JwtFilter.class.getName());

	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;

	public JwtFilter(JwtUtil jwtUtil, @Lazy UserDetailsService uds) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = uds;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		try {
			if (CommonConstants.PUBLIC_PATHS.contains(request.getRequestURI())) {
				chain.doFilter(request, response);
			} else {
				String header = request.getHeader("Authorization");
				if (header != null && header.startsWith("Bearer ")) {
					setAuthenticationContext(request, header);
				} else {
					throw new CustomGraphQLException(401, "Auth token not present");
				}
				chain.doFilter(request, response);
			}
		} catch (CustomGraphQLException ex) {
			log.warn("UnAuthorized Access : {}", ex.getMessage());
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			response.getWriter().write(String.format("{\"Message\":\"%s\",\"StatusCode\":%d}", ex.getMessage(), ex.getStatusCode()));
		}
	}

	private void setAuthenticationContext(HttpServletRequest request, String header) {
		String token = header.substring(7);
		String username = extractUserName(token);
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			if (jwtUtil.validateJwtToken(token)) {
				var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(auth);
			} else {
				throw new CustomGraphQLException(401, "InValid Auth token Or Token Expired");
			}
		}
	}
	
	/**
	 * Function to extract UserName from JWT token
	 * @param token
	 * @return
	 */
	private String extractUserName(String token) {
		try {
			return jwtUtil.getUsernameFromToken(token);
		} catch (ExpiredJwtException e) {
			throw new CustomGraphQLException(401, "JWT token is Expired");
		} catch (Exception e) {
			throw new CustomGraphQLException(500, e.getMessage());
		}
	}

}
