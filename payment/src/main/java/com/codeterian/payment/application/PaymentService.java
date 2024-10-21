package com.codeterian.payment.application;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeterian.common.exception.CommonErrorCode;
import com.codeterian.common.exception.RestApiException;
import com.codeterian.common.infrastructure.dto.payment.PaymentAfterValidateRequestDto;
import com.codeterian.common.infrastructure.dto.payment.PaymentBeforeValidateRequestDto;
import com.codeterian.common.infrastructure.enums.PaymentStatus;
import com.codeterian.payment.domain.entity.payment.Payment;
import com.codeterian.payment.domain.repository.PaymentRepository;
import com.codeterian.payment.infrastructure.config.kafka.PaymentKafkaProducer;
import com.codeterian.payment.presentation.controller.exception.PaymentErrorCode;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Prepare;

@Service
public class PaymentService {

	private final PaymentRepository paymentRepository;

	private final IamportClient iamportClient;

	private final PaymentKafkaProducer paymentKafkaProducer;

	public PaymentService(PaymentRepository paymentRepository,
		@Value("${rest.api.key}") String restApiKey,
		@Value("${rest.api.secret}") String restApiSecret, PaymentKafkaProducer paymentKafkaProducer) {
		this.paymentRepository = paymentRepository;
		this.paymentKafkaProducer = paymentKafkaProducer;
		this.iamportClient = new IamportClient(restApiKey, restApiSecret);
	}


	// 사후 검증 : 저장되있는 price와 받은 데이터가 같은지 검증
	@Transactional
	public void paymentAfterValidate(PaymentAfterValidateRequestDto requestDto) throws
		IamportResponseException,
		IOException {

		IamportResponse<com.siot.IamportRestClient.response.Payment> iamportResponse = iamportClient.paymentByImpUid(
			requestDto.paymentUid());
		Payment payment = findByOrderId(UUID.fromString(iamportResponse.getResponse().getMerchantUid()));
		Integer amount = Integer.parseInt(requestDto.price());

		// 롤백 함수가 필요
		if(!"paid".equals(iamportResponse.getResponse().getStatus())){
			payment.updateStatus(PaymentStatus.FAILED);
			throw new RestApiException(PaymentErrorCode.INVALID_PAYMENT_AMOUNT);
		}

		// 롤백 함수가 필요
		if (!amount.equals(iamportResponse.getResponse().getAmount().intValue())) {
			payment.updateStatus(PaymentStatus.FAILED);
			throw new RestApiException(PaymentErrorCode.INVALID_PAYMENT_AMOUNT);
		}
		paymentKafkaProducer.successPaymentToOrder(UUID.fromString(requestDto.orderUid()));

		payment.updateStatus(PaymentStatus.PAID);
	}

	// 사전 검증
	@Transactional
	public void paymentBeforeValidate(PaymentBeforeValidateRequestDto message) throws
		IamportResponseException,
		IOException {
		Payment payment = paymentRepository.save(Payment.create(message.orderId(), message.price()));
		iamportClient.postPrepare(new PrepareData(payment.getId().toString(), new BigDecimal(payment.getPrice())));
	}

	@Transactional(readOnly = true)
	public Payment findByOrderId(UUID orderId) {
		return paymentRepository.findByOrderIdAndIsDeletedFalse(orderId).orElseThrow(
			() -> new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND));
	}

	public String paymentCheckBeforeValidate(String orderId, Integer price) throws IamportResponseException, IOException {
		int code = iamportClient.getPrepare(orderId).getCode();
		if(code != 200){
			throw new RestApiException(PaymentErrorCode.FAILED_PAYMENT);
		}
		else {
			return "payment";
		}
	}
}
