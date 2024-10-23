package com.study.ecommerce.domain.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.ecommerce.domain.order.dto.PaymentCommand;
import com.study.ecommerce.domain.order.dto.PaymentInfo;
import com.study.ecommerce.infra.order.PaymentCommandRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentManager {

	private final PaymentCommandRepository paymentCommandRepository;

	public PaymentInfo create(PaymentCommand command) {
		return PaymentInfo.fromDomain(
			paymentCommandRepository.save(command.toDomain())
		);
	}
}
