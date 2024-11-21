package com.study.userservice.domain.eventbox;

import com.study.userservice.common.domain.TimeStamped;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class CommonEventBox extends TimeStamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String transactionKey; // 이벤트 고유 키

	private String topic; // 수신자(kafka topic)

	@Column(columnDefinition = "TEXT")
	private String payload; // 내용

	@Builder.Default
	@Enumerated(EnumType.STRING)
	private Status status = Status.PENDING; // 상태

	public void failure() {
		this.status = CommonEventBox.Status.FAILED;
	}

	@Getter
	public enum Status {

		PENDING("대기", 0, ""),
		PROCESSED("진행완료", 10, ""),
		FAILED("실패", 20, "");

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
