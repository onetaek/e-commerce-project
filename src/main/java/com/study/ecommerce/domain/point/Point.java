package com.study.ecommerce.domain.point;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
	name = "HANGHAE_POINTS"
)
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Builder.Default
	private Long amount = 0L;

	@Column(nullable = false)
	private String userId;

	public void charge(long amount) {
		this.amount = this.amount + amount;
	}

	public void use(Long useAmount) {
		if (amount < useAmount)
			throw PointException.exceed();
		this.amount = this.amount - useAmount;
	}

	@Getter
	public enum Type {
		CHARGE("충전", 0, ""),
		USE("사용", 10, "");

		private final String code;
		private final String displayValue;
		private final Integer sequence;
		private final String description;

		Type(String displayValue, Integer sequence, String description) {
			this.code = this.name();
			this.displayValue = displayValue;
			this.sequence = sequence;
			this.description = description;
		}
	}
}
