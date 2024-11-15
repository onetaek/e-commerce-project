package com.study.userservice.presentation.point;

import com.study.userservice.domain.point.PointCommand;
import com.study.userservice.domain.point.PointInfo;

public class PointDto {

	public record Response(
		Long id,
		Long amount,
		String userId
	) {
		public static Response fromInfo(PointInfo.Info info) {
			return new Response(
				info.id(),
				info.amount(),
				info.userId()
			);
		}
	}

	public record Request(
		String userId,
		long amount
	) {
		public PointCommand.Charge toCommand() {
			return new PointCommand.Charge(userId, amount);
		}
	}
}
