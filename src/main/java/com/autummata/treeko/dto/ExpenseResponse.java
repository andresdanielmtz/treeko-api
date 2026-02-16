package com.autummata.treeko.dto;

import java.math.BigDecimal;

import com.autummata.treeko.model.ExpenseType;

public record ExpenseResponse(Long id, String name, BigDecimal cost, ExpenseType type) {
}
