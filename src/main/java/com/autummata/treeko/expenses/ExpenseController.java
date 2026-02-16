package com.autummata.treeko.expenses;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.autummata.treeko.dto.ExpenseCreateRequest;
import com.autummata.treeko.dto.ExpenseResponse;
import com.autummata.treeko.dto.ExpenseUpdateRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/expenses", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExpenseController {
	private final ExpenseService expenseService;

	public ExpenseController(ExpenseService expenseService) {
		this.expenseService = expenseService;
	}

	@GetMapping
	public List<ExpenseResponse> list(@AuthenticationPrincipal UserDetails user) {
		return expenseService.list(user.getUsername());
	}

	@GetMapping("/{id}")
	public ExpenseResponse get(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
		return expenseService.get(id, user.getUsername());
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ExpenseResponse create(@Valid @RequestBody ExpenseCreateRequest request,
			@AuthenticationPrincipal UserDetails user) {
		return expenseService.create(request, user.getUsername());
	}

	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ExpenseResponse update(@PathVariable Long id, @Valid @RequestBody ExpenseUpdateRequest request,
			@AuthenticationPrincipal UserDetails user) {
		return expenseService.update(id, request, user.getUsername());
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
		expenseService.delete(id, user.getUsername());
	}
}
