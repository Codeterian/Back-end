package com.codeterian.order.presentation.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record OrderModifyRequestDto(
	@NotNull(message = "price is not Null")
	Integer price

) {
}
