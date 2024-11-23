package com.study.orderservice.presentation.order;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.study.orderservice.common.exception.SendAlertException;
import com.study.orderservice.common.utility.JacksonUtils;
import com.study.orderservice.domain.eventbox.CommonEventBox;
import com.study.orderservice.domain.eventbox.Outbox;
import com.study.orderservice.domain.eventbox.OutboxCommand;
import com.study.orderservice.domain.eventbox.OutboxService;
import com.study.orderservice.domain.order.OrderEventCommand;
import com.study.orderservice.domain.order.OrderEventService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventListener {

	@Value("${application.topic.order.order-created}")
	private String orderOrderCreatedTopic;

	private final OutboxService outboxService;
	private final OrderEventService orderEventService;

	/**
	 * <h1>Outbox 테이블에 주문생성 이벤트 기록</h1>
	 * <ul>
	 *     <li>BEFORE_COMMIT 을 사용하여 비즈니르로직과 같은 트랜잭션으로 수행</li>
	 *     <li>Status 는 PENDING(보류) 으로 저장</li>
	 * </ul>
	 */
	@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
	public void recordOutbox(OrderEventCommand.OrderCreate event) {
		outboxService.record(
			new OutboxCommand.Create(
				String.valueOf(event.orderId()),
				CommonEventBox.Status.PENDING,
				Outbox.EventType.ORDER_CREATE,
				orderOrderCreatedTopic,
				JacksonUtils.convertObjectToJsonString(event)
			)
		);
	}

	/**
	 * <h1>Kafka 에 주문생성 메시지 전송</h1>
	 */
	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void sendOrderCreateEvent(OrderEventCommand.OrderCreate event) {
		try {
			orderEventService.send(
				orderOrderCreatedTopic,
				event.orderId().toString(),
				JacksonUtils.convertObjectToJsonString(event)
			);
		} catch (Exception e) {
			throw new SendAlertException("주문생성 이벤트 메시지 전송 실패");
		}
	}

}
