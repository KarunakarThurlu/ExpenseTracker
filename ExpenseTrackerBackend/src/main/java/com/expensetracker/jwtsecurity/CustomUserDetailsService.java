package com.expensetracker.jwtsecurity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.expensetracker.entity.User;
import com.expensetracker.service.UserService;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=userService.findByEmail(username);
		if(user==null) {
			throw new UsernameNotFoundException("Invalid username or Password");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),getAuthority(user));
	}

	private Collection<? extends GrantedAuthority> getAuthority(User user) {
		 Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		 user.getRoles().forEach(role->{authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName()));});
		return authorities;
	}
}

