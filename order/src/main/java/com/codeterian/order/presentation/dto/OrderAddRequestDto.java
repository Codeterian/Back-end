package com.codeterian.order.presentation.dto;

import java.util.UUID;


public record OrderAddRequestDto (

	Long userId,
	UUID ticketId,
	Integer price,
	Integer quantity
){ }
