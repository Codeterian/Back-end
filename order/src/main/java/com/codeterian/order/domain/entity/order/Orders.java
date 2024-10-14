package com.codeterian.order.domain.entity.order;

import java.io.Serializable;
import java.util.UUID;

import com.codeterian.common.infrastructure.entity.BaseEntity;
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
public class Orders extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	private Long userId;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	private Money totalPrice;

	public static Orders add(Integer totalPrice, Long userId) {
		return Orders.builder()
			.userId(userId)
			.totalPrice(Money.create(totalPrice))
			.orderStatus(OrderStatus.PENDING)
			.build();
	}

	public void success() {
		this.orderStatus = OrderStatus.COMPLETED;
	}

	public void fail() {
		this.orderStatus = OrderStatus.FAILED;
	}

	public void delete(Long userId) {
		this.delete(userId);
	}

	public void update(OrderModifyRequestDto requestDto) {
		this.totalPrice = Money.create(requestDto.price());
		this.userId = requestDto.userId();
	}

	public void updateStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
}
