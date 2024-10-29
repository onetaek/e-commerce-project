package com.study.ecommerce.domain.point;

public class PointInfo {

	public record Info(
		Long id,
		Long amount,
		String userId
	) {

		public static Info from(Point domain) {
			return new Info(
				domain.getId(),
				domain.getAmount(),
				domain.getUserId()
			);
		}
	}

}
