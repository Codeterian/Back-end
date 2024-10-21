package com.codeterian.payment.presentation.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import com.codeterian.common.infrastructure.util.CurrentPassport;
import com.codeterian.common.infrastructure.util.Passport;
import com.codeterian.payment.application.PaymentService;
import com.siot.IamportRestClient.exception.IamportResponseException;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentViewController {

	PaymentService paymentService;

	// 사전 검증 실패를 고려
	@GetMapping("/view")
	public String paymentPage(
		@RequestParam(value = "orderId") String orderId,
		@RequestParam(value = "price") Integer price,
		Model model) throws IamportResponseException, IOException {
		model.addAttribute("merchantUid", orderId);
		model.addAttribute("amount", price);
		// 사전 검증 정보를 확인 -> 주문 상태값이 Approved가 아닐경우 사전검증 데이터가 없다.
		return paymentService.paymentCheckBeforeValidate(orderId, price);
	}
}
