package com.autummata.treeko.expenses;

import java.util.List;

import org.springframework.stereotype.Service;

import com.autummata.treeko.dto.ExpenseCreateRequest;
import com.autummata.treeko.dto.ExpenseResponse;
import com.autummata.treeko.dto.ExpenseUpdateRequest;
import com.autummata.treeko.model.Expense;
import com.autummata.treeko.repo.ExpenseRepository;
import com.autummata.treeko.repo.UserAccountRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ExpenseService {
	private final ExpenseRepository expenses;
	private final UserAccountRepository users;

	public ExpenseService(ExpenseRepository expenses, UserAccountRepository users) {
		this.expenses = expenses;
		this.users = users;
	}

	public List<ExpenseResponse> list(String username) {
		return expenses.findAllByUserUsernameOrderByIdDesc(username).stream().map(ExpenseService::toResponse).toList();
	}

	public ExpenseResponse get(Long id, String username) {
		Expense expense = expenses.findByIdAndUserUsername(id, username)
				.orElseThrow(() -> new EntityNotFoundException("Expense not found"));
		return toResponse(expense);
	}

	public ExpenseResponse create(ExpenseCreateRequest request, String username) {
		var user = users.findByUsername(username)
				.orElseThrow(() -> new EntityNotFoundException("User not found"));
		Expense saved = expenses.save(new Expense(request.name(), request.cost(), request.type(), user));
		return toResponse(saved);
	}

	public ExpenseResponse update(Long id, ExpenseUpdateRequest request, String username) {
		Expense expense = expenses.findByIdAndUserUsername(id, username)
				.orElseThrow(() -> new EntityNotFoundException("Expense not found"));
		expense.setName(request.name());
		expense.setCost(request.cost());
		expense.setType(request.type());
		Expense saved = expenses.save(expense);
		return toResponse(saved);
	}

	public void delete(Long id, String username) {
		Expense expense = expenses.findByIdAndUserUsername(id, username)
				.orElseThrow(() -> new EntityNotFoundException("Expense not found"));
		expenses.delete(expense);
	}

	private static ExpenseResponse toResponse(Expense expense) {
		return new ExpenseResponse(expense.getId(), expense.getName(), expense.getCost(), expense.getType());
	}
}
