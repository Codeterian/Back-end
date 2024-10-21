package com.codeterian.order.domain.entity.order;

import java.io.Serializable;
import java.util.UUID;

import com.codeterian.common.infrastructure.entity.BaseEntity;
import com.codeterian.common.infrastructure.enums.OrderStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
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

	public void delete(Long userId) {
		this.delete(userId);
	}

	public void updateMoney(Integer money) {
		this.totalPrice = Money.create(money);
	}

	public void updateStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
}
