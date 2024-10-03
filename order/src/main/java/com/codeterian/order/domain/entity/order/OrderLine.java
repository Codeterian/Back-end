package com.codeterian.order.domain.entity.order;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderLine implements Serializable {

	private UUID ticketId;

	private Money price;

	private Integer quantity;

	private Money amounts;

	public OrderLine(UUID ticketId, Integer quantity, Integer price){
		this.ticketId = ticketId;
		this.quantity = quantity;
		this.price = Money.create(price);
		this.amounts = CalculateAmounts();
	}

	private Money CalculateAmounts() {
		return price.multiply(quantity);
	}

}
