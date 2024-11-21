package com.study.userservice.domain.eventbox;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Table(
	name = "HANGHAE_USER_OUTBOX"
)
@Getter
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Outbox extends CommonEventBox {

	public static int max_retryCount = 5;

	@Enumerated(EnumType.STRING)
	private EventType eventType;// 이벤트 유형

	@Builder.Default
	private int retryCount = 0;// 재시도 횟수

	private LocalDateTime processedAt;

	public void plusRetryCount() {
		this.retryCount++;
	}

	public boolean isMaxRetryCount() {
		return this.retryCount >= max_retryCount;
	}

	@Getter
	public enum EventType {

		USE_POINT("포인트 차감", 0, ""),
		USE_POINT_FAIlURE("포인트 차감 실패", 10, "");

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
