package com.expensetracker.jwtsecurity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

	private UserService userService;
	
	public CustomUserDetailsService(UserService userService) {
		this.userService=userService;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=userService.findByEmail(username);
		if(user==null) {
			throw new UsernameNotFoundException("Invalid username or Password");
		}
		return new UserDetailsImpl(user.getId(), user.getEmail(), user.getPassword(), getAuthority(user), user);
	}

	private Collection<? extends GrantedAuthority> getAuthority(User user) {
		 Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		 user.getRoles().forEach(role->{authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName()));});
		return authorities;
	}
}

