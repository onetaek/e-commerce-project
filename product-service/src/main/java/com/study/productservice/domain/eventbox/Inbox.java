package com.study.productservice.domain.eventbox;

import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Table(
	name = "HANGHAE_PRODUCT_INBOX"
)
@Getter
@Entity
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inbox extends CommonEventBox {

	private Outbox.EventType eventType;// 이벤트 유형

	@Getter
	public enum EventType {

		ORDER("주문생성", 0, "");

		private final String code;
		private final String displayValue;
		private final Integer sequence;
		private final String description;

		EventType(String displayValue, Integer sequence, String description) {
			this.code = this.name();
			this.displayValue = displayValue;
			this.sequence = sequence;
			this.description = description;
		}
	}
}
