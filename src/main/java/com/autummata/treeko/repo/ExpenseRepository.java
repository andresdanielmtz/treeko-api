package com.autummata.treeko.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autummata.treeko.model.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	List<Expense> findAllByUserUsernameOrderByIdDesc(String username);

	Optional<Expense> findByIdAndUserUsername(Long id, String username);
}
