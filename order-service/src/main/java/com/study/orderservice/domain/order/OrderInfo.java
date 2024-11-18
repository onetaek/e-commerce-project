package com.study.orderservice.domain.order;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.study.orderservice.domain.product.ProductInfo;

public class OrderInfo {

	public record Product(
		long productId,
		String productName,
		long productPrice,
		int orderAmount
	) {
		public static List<OrderInfo.Product> from(
			List<OrderResult.GroupByProduct> groupByOrders,
			List<ProductInfo.Data> productList
		) {
			Map<Long, ProductInfo.Data> productMap = productList.stream().collect(Collectors.toMap(
				ProductInfo.Data::id,
				v -> v
			));

			return groupByOrders.stream().map(v -> {
					ProductInfo.Data product = productMap.get(v.productId());
					return new OrderInfo.Product(
						product.id(),
						product.name(),
						product.price(),
						v.orderAmount()
					);
				}
			).collect(Collectors.toList());
		}
	}

}
