package com.autummata.treeko.user;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autummata.treeko.repo.UserAccountRepository;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
	private final UserAccountRepository users;

	public UserController(UserAccountRepository users) {
		this.users = users;
	}

	@GetMapping("/me")
	public MeResponse me(@AuthenticationPrincipal UserDetails principal) {
		var user = users.findByUsername(principal.getUsername())
				.orElseThrow(() -> new EntityNotFoundException("User not found"));
		return new MeResponse(user.getId(), user.getUsername(), user.getEmail());
	}

	public record MeResponse(Long id, String username, String email) {
	}
}
