package com.codeterian.payment.presentation.controller;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import com.codeterian.common.infrastructure.util.CurrentPassport;
import com.codeterian.common.infrastructure.util.Passport;

@Controller
@RequestMapping("/api/v1/payments")
public class PaymentViewController {

	@GetMapping("/view")
	public String paymentPage(
		@RequestParam(value = "orderId") String orderId,
		@RequestParam(value = "price") Integer price,
		@RequestParam(value = "userEmail") String buyerEmail,
		Model model) {
		model.addAttribute("merchantUid", orderId);
		model.addAttribute("amount", price);
		model.addAttribute("buyer_email", buyerEmail);
		return "payment";
	}
}
