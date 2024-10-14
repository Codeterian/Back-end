package com.codeterian.order.presentation.dto;

import java.util.UUID;

public record OrderModifyRequestDto(

	Long userId,
	Integer price

) {
}
