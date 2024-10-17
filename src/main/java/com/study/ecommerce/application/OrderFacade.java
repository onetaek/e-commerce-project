package com.study.ecommerce.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.ecommerce.domain.balance.service.BalanceValidationService;
import com.study.ecommerce.domain.order.PaymentStatus;
import com.study.ecommerce.domain.order.dto.OrderCommand;
import com.study.ecommerce.domain.order.dto.OrderInfo;
import com.study.ecommerce.domain.order.dto.OrderItemCommand;
import com.study.ecommerce.domain.order.dto.PaymentCommand;
import com.study.ecommerce.domain.order.service.OrderCreateService;
import com.study.ecommerce.domain.order.service.OrderItemCreateService;
import com.study.ecommerce.domain.order.service.OrderSendToDataPlatFormService;
import com.study.ecommerce.domain.order.service.PaymentCreateService;
import com.study.ecommerce.domain.product.service.ProductInventoryValidationService;
import com.study.ecommerce.presentation.order.dto.OrderAndPaymentCommand;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderFacade {

	private final BalanceValidationService balanceValidationService;
	private final ProductInventoryValidationService productInventoryValidationService;
	private final OrderCreateService orderCreateService;
	private final OrderItemCreateService orderItemCreateService;
	private final PaymentCreateService paymentCreateService;
	private final OrderSendToDataPlatFormService orderSendToDataPlatFormService;

	public OrderInfo order(OrderAndPaymentCommand command) {
		// 재고 유효성 검사
		productInventoryValidationService.validateAmount(command.productId(), command.amount());

		// 잔액 유효성 검사
		balanceValidationService.validateAmount(command.userId(), (long)command.amount() * command.price());

		// 주문 생성
		var orderInfo = orderCreateService.create(
			new OrderCommand(command.userId(), command.orderDate(), (long)command.price())
		);

		// TODO 주문 아이템 List 로 받을 수 있도록 리팩터링
		// 주문 아이템 생성
		orderItemCreateService.create(
			new OrderItemCommand(orderInfo.id(), command.productId(), command.amount(), command.price())
		);

		// 결재 생성
		paymentCreateService.create(
			new PaymentCommand(orderInfo.id(), (long)command.amount(), PaymentStatus.COMPLETE)
		);

		// 외부 데이터 플랫폼으로 전송
		orderSendToDataPlatFormService.send(orderInfo);

		return orderInfo;
	}

}
