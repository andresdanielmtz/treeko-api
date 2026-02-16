package com.autummata.treeko.dto;

import java.math.BigDecimal;

import com.autummata.treeko.model.ExpenseType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ExpenseCreateRequest(
		@Size(max = 120) String name,
		@NotNull @DecimalMin(value = "0.01") @Digits(integer = 17, fraction = 2) BigDecimal cost,
		@NotNull ExpenseType type) {
}
