package com.codeterian.order.domain.entity.order;

import java.io.Serializable;
import java.util.UUID;

import com.codeterian.order.domain.entity.status.OrderStatus;
import com.codeterian.order.presentation.dto.OrderAddRequestDto;
import com.codeterian.order.presentation.dto.OrderModifyRequestDto;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "orders")
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Orders implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	private Long userId;

	@Embedded
	private OrderLine orderLine;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	public static Orders add(OrderAddRequestDto requestDto) {
		return Orders.builder()
			.userId(requestDto.userId())
			.orderLine(new OrderLine(requestDto.ticketId(), requestDto.quantity(), requestDto.price()))
			.orderStatus(OrderStatus.PENDING)
			.build();
	}

	public void update(OrderModifyRequestDto requestDto) {
		this.orderLine = new OrderLine(requestDto.ticketId(), requestDto.quantity(), requestDto.price());
	}

	public void success() {
		this.orderStatus = OrderStatus.COMPLETED;
	}
	//
	// public void delete() {
	// 	this.isDeleted = false;
	// }
}
