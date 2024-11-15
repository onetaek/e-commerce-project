package com.study.orderservice.domain.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(
	name = "HANGHAE_PAYMENTS"
)
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long orderId;

	@Column(nullable = false)
	private Long price;

	@Enumerated(EnumType.STRING)
	private Status status;

	@Getter
	enum Status {

		STANDBY("대기", 0, ""),
		COMPLETE("완료", 10, "");

		private final String code;
		private final String displayValue;
		private final Integer sequence;
		private final String description;

		Status(String displayValue, Integer sequence, String description) {
			this.code = this.name();
			this.displayValue = displayValue;
			this.sequence = sequence;
			this.description = description;
		}
	}
}
