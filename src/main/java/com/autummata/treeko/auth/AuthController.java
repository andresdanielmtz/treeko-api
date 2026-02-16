package com.autummata.treeko.auth;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autummata.treeko.dto.AuthResponse;
import com.autummata.treeko.dto.LoginRequest;
import com.autummata.treeko.dto.RegisterRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
	public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
		return AuthResponse.bearer(authService.register(request));
	}

	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	public AuthResponse login(@Valid @RequestBody LoginRequest request) {
		return AuthResponse.bearer(authService.login(request));
	}
}
