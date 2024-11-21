package com.study.orderservice.presentation.order;

import java.time.LocalDateTime;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.study.orderservice.application.OrderFacade;
import com.study.orderservice.common.config.KafkaConsumerConfig;
import com.study.orderservice.common.utility.JacksonUtils;
import com.study.orderservice.domain.eventbox.CommonEventBox;
import com.study.orderservice.domain.eventbox.Outbox;
import com.study.orderservice.domain.eventbox.OutboxCommand;
import com.study.orderservice.domain.eventbox.OutboxService;
import com.study.orderservice.domain.order.OrderService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderConsumer {

	private final OutboxService outboxService;
	private final OrderFacade orderFacade;
	private final OrderService orderService;

	@KafkaListener(
		topics = "${application.topic.order.order-created}",
		containerFactory = KafkaConsumerConfig.KAFKA_LISTENER_FACTORY_GROUP_OUTBOX
	)
	public void consumingOrderCreateMessage(ConsumerRecord<String, String> consumerRecord) {

		outboxService.update(
			new OutboxCommand.Update(
				CommonEventBox.Status.PROCESSED,
				LocalDateTime.now(),
				new OutboxCommand.Update.Where(
					consumerRecord.key(),
					CommonEventBox.Status.PENDING,
					Outbox.EventType.ORDER_CREATE
				)
			)
		);
	}

	@KafkaListener(
		topics = "${application.topic.user.point-use}",
		containerFactory = KafkaConsumerConfig.KAFKA_LISTENER_FACTORY_GROUP_LOGIC
	)
	public void consumingPointUseMessage(ConsumerRecord<String, String> consumerRecord) {

		var message = JacksonUtils.convertJsonStringToObject(
			consumerRecord.value(),
			OrderDto.UserPointUseMessage.class
		);

		orderFacade.sendOrderProcessedAlert(
			message.toCommand()
		);
	}

	@KafkaListener(
		topics = "${application.topic.product.inventory-deduct-failure}",
		containerFactory = KafkaConsumerConfig.KAFKA_LISTENER_FACTORY_GROUP_LOGIC
	)
	public void consumingInventoryDeductFailureMessage(ConsumerRecord<String, String> consumerRecord) {

		var message = JacksonUtils.convertJsonStringToObject(
			consumerRecord.value(),
			OrderDto.InventoryDeductFailureMessage.class
		);

		orderFacade.cancelOrderAndSendErrorMessage(
			message.toCommand(),
			message.errorMessage()
		);
	}
}
