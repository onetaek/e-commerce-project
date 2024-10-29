package com.study.ecommerce.domain.point;

public class PointCommand {
	public record Charge(
		String userId,
		long amount
	) {
	}
}
