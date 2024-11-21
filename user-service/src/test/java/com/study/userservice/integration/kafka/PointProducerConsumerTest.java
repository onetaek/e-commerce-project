package com.study.userservice.integration.kafka;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.study.userservice.domain.eventbox.CommonEventBox;
import com.study.userservice.domain.eventbox.Outbox;
import com.study.userservice.domain.point.PointEventCommand;
import com.study.userservice.domain.point.PointEventService;
import com.study.userservice.infra.eventbox.OutboxJpaRepository;
import com.study.userservice.integration.TestContainerEnvironment;

public class PointProducerConsumerTest extends TestContainerEnvironment {

	@Autowired
	PointEventService pointEventService;

	@Autowired
	OutboxJpaRepository outboxJpaRepository;

	@DisplayName("포인트 사용 이벤트를 발행하면 비동기적으로 발행된 메시지를 직접 소비하여 진행완료 상태로 바꿀 수 있다.")
	@Test
	void publishUserPointEventAndConsumingMessage() {
		// given
		PointEventCommand.Use command = new PointEventCommand.Use(
			"1111",
			"user1",
			1L,
			1000L
		);

		// when
		pointEventService.publishEvent(command);

		// then
		Awaitility.await()
			.atMost(10, TimeUnit.SECONDS)
			.pollInterval(1, TimeUnit.SECONDS)
			.until(() -> outboxJpaRepository.findAll().get(0).getStatus() == CommonEventBox.Status.PROCESSED);

		Outbox outbox = outboxJpaRepository.findAll().get(0);
		assertThat(outbox.getStatus()).isEqualTo(CommonEventBox.Status.PROCESSED);
		assertThat(outbox.getProcessedAt()).isNotNull();
	}

	@DisplayName("포인트 사용 실패 이벤트를 발행하면 비동기적으로 발행된 메시지를 직접 소비하여 진행완료 상태로 바꿀 수 있다.")
	@Test
	void publishUsePointFailureEventAndConsumingMessage() {
		// given
		PointEventCommand.UseFailure command = new PointEventCommand.UseFailure(
			"1111",
			"포인트 사용 실패",
			"user1",
			1L,
			List.of(
				new PointEventCommand.UseFailure.Product(1L, 1, 1000),
				new PointEventCommand.UseFailure.Product(2L, 2, 2000)
			)
		);

		// when
		pointEventService.publishEvent(command);

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
