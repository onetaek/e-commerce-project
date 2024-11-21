package com.study.productservice.presentation.product;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.study.productservice.common.utility.JacksonUtils;
import com.study.productservice.domain.eventbox.CommonEventBox;
import com.study.productservice.domain.eventbox.Outbox;
import com.study.productservice.domain.eventbox.OutboxCommand;
import com.study.productservice.domain.eventbox.OutboxService;
import com.study.productservice.domain.product.ProductEventCommand;
import com.study.productservice.domain.product.ProductEventService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductEventListener {

	@Value("${application.topic.product.inventory-deduct}")
	private String productInventoryDeductTopic;

	@Value("${application.topic.product.inventory-deduct-failure}")
	private String productInventoryDeductFailureTopic;

	private final OutboxService outboxService;
	private final ProductEventService productEventService;

	@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
	public void recordOutbox(ProductEventCommand.InventoryDeduct event) {
		outboxService.record(
			new OutboxCommand.Create(
				event.transactionKey(),
				CommonEventBox.Status.PENDING,
				Outbox.EventType.INVENTORY_DEDUCT,
				productInventoryDeductTopic,
				JacksonUtils.convertObjectToJsonString(event)
			)
		);
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void sendInventoryDeductEvent(ProductEventCommand.InventoryDeduct event) {
		productEventService.send(
			productInventoryDeductTopic,
			event.transactionKey(),
			JacksonUtils.convertObjectToJsonString(event)
		);
	}

	@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
	public void recordOutbox(ProductEventCommand.InventoryDeductFailure event) {
		outboxService.record(
			new OutboxCommand.Create(
				event.transactionKey(),
				CommonEventBox.Status.PENDING,
				Outbox.EventType.INVENTORY_DEDUCT_FAILURE,
				productInventoryDeductFailureTopic,
				JacksonUtils.convertObjectToJsonString(event)
			)
		);
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void sendInventoryDeductFailureEvent(ProductEventCommand.InventoryDeductFailure event) {
		productEventService.send(
			productInventoryDeductFailureTopic,
			event.transactionKey(),
			JacksonUtils.convertObjectToJsonString(event)
		);
	}

}
