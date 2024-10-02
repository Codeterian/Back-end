package com.codeterian.order.domain.entity.order;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Money implements Serializable {

	private Integer value;

	public static Money create(Integer price) {
		return new Money(price);
	}

	public Money add(Money money){
		return new Money(this.value + money.value);
	}

	public Money multiply(Integer multiplier){
		return new Money(this.value * multiplier);
	}

}
