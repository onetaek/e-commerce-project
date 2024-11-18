package com.study.orderservice.domain.order;

public class OrderResult {

	public record GroupByProduct(
		long productId,
		int orderAmount
	) {
	}
}
