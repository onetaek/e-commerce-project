package com.study.userservice.domain.eventbox;

import java.time.LocalDateTime;

public class OutboxCommand {
	public record Create(
		String transactionKey,
		CommonEventBox.Status status,
		Outbox.EventType eventType,
		String topic,
		String payload
	) {
		public Outbox toOutbox() {
			return Outbox.builder()
				.transactionKey(transactionKey)
				.status(status)
				.eventType(eventType)
				.topic(topic)
				.payload(payload)
				.build();
		}
	}

	public record Update(
		CommonEventBox.Status status,
		LocalDateTime processedAt,
		Where where
	) {
		public record Where(
			String transactionKey,
			CommonEventBox.Status status,
			Outbox.EventType eventType
		) {
		}
	}
}
