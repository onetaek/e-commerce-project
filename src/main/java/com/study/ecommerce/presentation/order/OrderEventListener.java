package com.study.ecommerce.presentation.order;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.study.ecommerce.infra.order.OrderSlackSender;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderEventListener {

	private final OrderSlackSender orderSlackSender;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void send(OrderDto.Event event) {
		orderSlackSender.sendEvent(event);
	}

}
