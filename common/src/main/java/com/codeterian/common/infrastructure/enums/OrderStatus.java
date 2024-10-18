package com.codeterian.common.infrastructure.enums;

public enum OrderStatus {
	PENDING,            // 주문 대기
	APPROVED,           // 주문은 완료 결제 대기
	COMPLETED,          // 주문, 결제 완료
	FAILED,             // 주문 실패
	CANCELLED            // 결제 취소
}
