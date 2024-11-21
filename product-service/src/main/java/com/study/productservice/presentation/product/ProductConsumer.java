package com.study.productservice.presentation.product;

import java.time.LocalDateTime;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.study.productservice.application.ProductFacade;
import com.study.productservice.common.config.KafkaConsumerConfig;
import com.study.productservice.common.utility.JacksonUtils;
import com.study.productservice.domain.eventbox.CommonEventBox;
import com.study.productservice.domain.eventbox.Outbox;
import com.study.productservice.domain.eventbox.OutboxCommand;
import com.study.productservice.domain.eventbox.OutboxService;
import com.study.productservice.domain.product.ProductEventCommand;
import com.study.productservice.domain.product.ProductEventService;
import com.study.productservice.domain.product.ProductException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductConsumer {

	private final ProductFacade productFacade;
	private final ProductEventService productEventService;
	private final OutboxService outboxService;

	/**
	 * <h1>주문 생성 이벤트 메시지 수신</h1>
	 * <ul>
	 *     <li>주문 생성 이벤트 메시지를 수신하여 상품 재고를 차감</li>
	 * </ul>
	 */
	@KafkaListener(
		topics = "${application.topic.order.order-created}",
		containerFactory = KafkaConsumerConfig.KAFKA_LISTENER_FACTORY_GROUP_LOGIC
	)
	public void consumingOrderCreatedMessage(ConsumerRecord<String, String> consumerRecord) {

		var key = consumerRecord.key();
		var message = JacksonUtils.convertJsonStringToObject(
			consumerRecord.value(),
			ProductDto.OrderCreatedMessage.class
		);
		String errorMessage = null;

		try {
			productFacade.deduct(message.toCommand(), key);
		} catch (ProductException e) {
			errorMessage = e.getMessage();
		} catch (Exception e) {
			errorMessage = "재고차감중 알 수 없는 오류가 발생했습니다.";
			log.error(errorMessage, e);
		}
		if (errorMessage != null) {
			productEventService.publishEvent(
				new ProductEventCommand.InventoryDeductFailure(
					key,
					errorMessage,
					message.orderId(),
					message.userId()
				)
			);
		}
	}

	@KafkaListener(
		topics = "${application.topic.product.inventory-deduct}",
		containerFactory = KafkaConsumerConfig.KAFKA_LISTENER_FACTORY_GROUP_OUTBOX
	)
	public void consumingInventoryDeductMessage(ConsumerRecord<String, String> consumerRecord) {

		outboxService.update(
			new OutboxCommand.Update(
				CommonEventBox.Status.PROCESSED,
				LocalDateTime.now(),
				new OutboxCommand.Update.Where(
					consumerRecord.key(),
					CommonEventBox.Status.PENDING,
					Outbox.EventType.INVENTORY_DEDUCT
				)
			)
		);
	}

	@KafkaListener(
		topics = "${application.topic.user.point-use-failure}",
		containerFactory = KafkaConsumerConfig.KAFKA_LISTENER_FACTORY_GROUP_LOGIC
	)
	public void consumingPointUseFailureMessage(ConsumerRecord<String, String> consumerRecord) {

		var key = consumerRecord.key();
		var message = JacksonUtils.convertJsonStringToObject(
			consumerRecord.value(),
			ProductDto.PointUseFailureMessage.class
		);

		productFacade.recovery(
			message.toCommand(),
			key,
			message.errorMessage()
		);
	}

	@KafkaListener(
		topics = "${application.topic.product.inventory-deduct-failure}",
		containerFactory = KafkaConsumerConfig.KAFKA_LISTENER_FACTORY_GROUP_OUTBOX
	)
	public void consumingInventoryDeductFailureMessage(ConsumerRecord<String, String> consumerRecord) {

		outboxService.update(
			new OutboxCommand.Update(
				CommonEventBox.Status.PROCESSED,
				LocalDateTime.now(),
				new OutboxCommand.Update.Where(
					consumerRecord.key(),
					CommonEventBox.Status.PENDING,
					Outbox.EventType.INVENTORY_DEDUCT_FAILURE
				)
			)
		);
	}
}
