package com.autummata.treeko.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.autummata.treeko.dto.LoginRequest;
import com.autummata.treeko.dto.RegisterRequest;
import com.autummata.treeko.model.UserAccount;
import com.autummata.treeko.repo.UserAccountRepository;
import com.autummata.treeko.security.JwtService;

@Service
public class AuthService {
	private final UserAccountRepository users;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	public AuthService(UserAccountRepository users, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager, JwtService jwtService) {
		this.users = users;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}

	public String register(RegisterRequest request) {
		if (users.existsByUsername(request.username())) {
			throw new IllegalArgumentException("Username already taken");
		}
		if (users.existsByEmail(request.email())) {
			throw new IllegalArgumentException("Email already registered");
		}

		String hash = passwordEncoder.encode(request.password());
		users.save(new UserAccount(request.username(), request.email(), hash));
		return jwtService.generateToken(request.username());
	}

	public String login(LoginRequest request) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.username(), request.password()));
		} catch (AuthenticationException ex) {
			throw new IllegalArgumentException("Invalid username or password");
		}
		return jwtService.generateToken(request.username());
	}
}
