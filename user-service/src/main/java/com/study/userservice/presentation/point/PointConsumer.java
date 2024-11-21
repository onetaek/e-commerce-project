package com.study.userservice.presentation.point;

import java.time.LocalDateTime;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.study.userservice.application.PointFacade;
import com.study.userservice.common.config.KafkaConsumerConfig;
import com.study.userservice.common.utility.JacksonUtils;
import com.study.userservice.domain.eventbox.CommonEventBox;
import com.study.userservice.domain.eventbox.Outbox;
import com.study.userservice.domain.eventbox.OutboxCommand;
import com.study.userservice.domain.eventbox.OutboxService;
import com.study.userservice.domain.point.PointEventService;
import com.study.userservice.domain.point.PointException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PointConsumer {

	private final PointFacade pointFacade;
	private final PointEventService pointEventService;
	private final OutboxService outboxService;

	@KafkaListener(
		topics = "${application.topic.product.inventory-deduct}",
		containerFactory = KafkaConsumerConfig.KAFKA_LISTENER_FACTORY_GROUP_LOGIC
	)
	public void consumingInventoryDeductMessage(ConsumerRecord<String, String> consumerRecord) {

		var message = JacksonUtils.convertJsonStringToObject(
			consumerRecord.value(),
			PointDto.InventoryDeductMessage.class
		);
		String errorMessage = null;

		try {
			pointFacade.usePoint(
				message.toCommand(),
				consumerRecord.key()
			);
		} catch (PointException e) {
			errorMessage = e.getMessage();
		} catch (Exception e) {
			errorMessage = "포인트 사용중 알 수 없는 오류가 발생했습니다.";
			log.error(errorMessage, e);
		}

		if (errorMessage != null) {
			pointEventService.publishEvent(
				message.toUseFailureCommand(errorMessage)
			);
		}
	}

	@KafkaListener(
		topics = "${application.topic.user.point-use}",
		containerFactory = KafkaConsumerConfig.KAFKA_LISTENER_FACTORY_GROUP_OUTBOX
	)
	public void consumingPointUseMessage(ConsumerRecord<String, String> consumerRecord) {

		var key = consumerRecord.key();
		var message = JacksonUtils.convertJsonStringToObject(
			consumerRecord.value(),
			PointDto.UsePointMessage.class
		);

		// 대기 상태였던 Outbox 데이터를 진행완료 상태로 수정
		outboxService.update(
			new OutboxCommand.Update(
				CommonEventBox.Status.PROCESSED,
				LocalDateTime.now(),
				new OutboxCommand.Update.Where(
					key,
					CommonEventBox.Status.PENDING,
					Outbox.EventType.USE_POINT
				)
			)
		);
	}

	@KafkaListener(
		topics = "${application.topic.user.point-use-failure}",
		containerFactory = KafkaConsumerConfig.KAFKA_LISTENER_FACTORY_GROUP_OUTBOX
	)
	public void consumingPointUseFailureMessage(ConsumerRecord<String, String> consumerRecord) {

		var key = consumerRecord.key();
		var message = JacksonUtils.convertJsonStringToObject(
			consumerRecord.value(),
			PointDto.UsePointFailureMessage.class
		);

		outboxService.update(
			new OutboxCommand.Update(
				CommonEventBox.Status.PROCESSED,
				LocalDateTime.now(),
				new OutboxCommand.Update.Where(
					key,
					CommonEventBox.Status.PENDING,
					Outbox.EventType.USE_POINT_FAIlURE
				)
			)
		);
	}

}
