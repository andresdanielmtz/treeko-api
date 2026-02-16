package com.autummata.treeko.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.autummata.treeko.repo.UserAccountRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserAccountRepository users;

	public UserDetailsServiceImpl(UserAccountRepository users) {
		this.users = users;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var user = users.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		return new User(user.getUsername(), user.getPasswordHash(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
	}
}
