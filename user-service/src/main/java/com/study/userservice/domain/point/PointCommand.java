package com.study.userservice.domain.point;

public class PointCommand {
	public record Charge(
		String userId,
		long amount
	) {
	}
}