package com.study.ecommerce.application.order;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.ecommerce.domain.balance.service.BalanceValidator;
import com.study.ecommerce.domain.order.PaymentStatus;
import com.study.ecommerce.domain.order.dto.OrderCommand;
import com.study.ecommerce.domain.order.dto.OrderInfo;
import com.study.ecommerce.domain.order.dto.OrderItemCommand;
import com.study.ecommerce.domain.order.dto.PaymentCommand;
import com.study.ecommerce.domain.order.service.OrderItemManager;
import com.study.ecommerce.domain.order.service.OrderManager;
import com.study.ecommerce.domain.order.service.OrderSendManager;
import com.study.ecommerce.domain.order.service.PaymentManager;
import com.study.ecommerce.domain.product.service.ProductValidator;
import com.study.ecommerce.presentation.order.dto.OrderAndPaymentCommand;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderAndPaymentService {

	private final BalanceValidator balanceValidator;
	private final ProductValidator productValidator;
	private final OrderManager orderManager;
	private final OrderItemManager orderItemManager;
	private final PaymentManager paymentManager;
	private final OrderSendManager orderSendManager;

	public OrderInfo order(OrderAndPaymentCommand command) {
		// 재고 유효성 검사
		productValidator.validateAmount(command.productId(), command.amount());

		// 잔액 유효성 검사
		balanceValidator.validateAmount(command.userId(), (long)command.amount() * command.price());

		// 주문 생성
		var orderInfo = orderManager.create(
			new OrderCommand(command.userId(), command.orderDate(), (long)command.price())
		);

		// 주문 아이템 생성
		orderItemManager.create(
			new OrderItemCommand(orderInfo.id(), command.productId(), command.amount(), command.price())
		);

		// 결재 생성
		paymentManager.create(
			new PaymentCommand(orderInfo.id(), (long)command.amount(), PaymentStatus.COMPLETE)
		);

		// 외부 데이터 플랫폼으로 전송
		orderSendManager.send(orderInfo);

		return orderInfo;
	}

}
