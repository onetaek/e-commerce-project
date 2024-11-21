package com.study.productservice.integration.kafka;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.study.productservice.domain.eventbox.CommonEventBox;
import com.study.productservice.domain.eventbox.Outbox;
import com.study.productservice.domain.product.ProductEventCommand;
import com.study.productservice.domain.product.ProductEventService;
import com.study.productservice.infra.eventbox.OutboxJpaRepository;
import com.study.productservice.integration.TestContainerEnvironment;

public class ProductProducerConsumerTest extends TestContainerEnvironment {

	@Autowired
	ProductEventService orderEventService;

	@Autowired
	OutboxJpaRepository outboxJpaRepository;

	@DisplayName("재고차감 이벤트를 발행하면 비동기적으로 발행된 메시지를 직접 소비하여 진행완료 상태로 바꿀 수 있다.")
	@Test
	void publishInventoryDeductEventAndConsumingMessage() {
		// given
		ProductEventCommand.InventoryDeduct command = new ProductEventCommand.InventoryDeduct(
			"111",
			1L,
			"user1",
			100,
			List.of(
				new ProductEventCommand.InventoryDeduct.Product(1L, 1, 1000),
				new ProductEventCommand.InventoryDeduct.Product(2L, 2, 2000)
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

	@DisplayName("재고차감 실패 이벤트를 발행하면 비동기적으로 발행된 메시지를 직접 소비하여 진행완료 상태로 바꿀 수 있다.")
	@Test
	void publishInventoryDeductFailureEventAndConsumingMessage() {
		// given
		ProductEventCommand.InventoryDeductFailure command = new ProductEventCommand.InventoryDeductFailure(
			"111",
			"재고차감 실패",
			1L,
			"user1"
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
