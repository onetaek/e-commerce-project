package com.study.orderservice.integration.kafka;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.study.orderservice.domain.eventbox.CommonEventBox;
import com.study.orderservice.domain.eventbox.Outbox;
import com.study.orderservice.domain.order.OrderEventCommand;
import com.study.orderservice.domain.order.OrderEventService;
import com.study.orderservice.infra.eventbox.OutboxJpaRepository;
import com.study.orderservice.integration.TestContainerEnvironment;

public class OrderProducerConsumerTest extends TestContainerEnvironment {

	@Autowired
	OrderEventService orderEventService;

	@Autowired
	OutboxJpaRepository outboxJpaRepository;

	@DisplayName("주문 생성 이벤트를 발행하면 비동기적으로 발행된 메시지를 직접 소비하여 진행완료 상태로 바꿀 수 있다.")
	@Test
	void publishOrderCreateEventAndConsumingMessage() {
		// given
		OrderEventCommand.OrderCreate command = new OrderEventCommand.OrderCreate(
			"user3",
			1L,
			List.of(
				new OrderEventCommand.OrderCreate.OrderItem(1L, 1, 1000),
				new OrderEventCommand.OrderCreate.OrderItem(2L, 2, 2000)
			)
		);

		// when
		orderEventService.publishEvent(command);

		// then
		Awaitility.await()
			.atMost(10, TimeUnit.SECONDS)
			.pollInterval(1, TimeUnit.SECONDS)
			.until(() -> outboxJpaRepository.findAll().get(0).getStatus() == CommonEventBox.Status.PROCESSED);

		Outbox outbox = outboxJpaRepository.findAll().get(0);
		assertThat(outbox.getStatus()).isEqualTo(CommonEventBox.Status.PROCESSED);
		assertThat(outbox.getProcessedAt()).isNotNull();
	}
}
