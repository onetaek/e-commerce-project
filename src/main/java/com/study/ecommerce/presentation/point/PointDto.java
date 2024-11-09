package com.study.ecommerce.presentation.point;

import com.study.ecommerce.domain.point.PointCommand;
import com.study.ecommerce.domain.point.PointInfo;

public class PointDto {

	public record Response(
		Long id,
		Long amount,
		String userId
	) {
		public static PointDto.Response fromInfo(PointInfo.Info info) {
			return new PointDto.Response(
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
