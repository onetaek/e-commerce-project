package com.study.ecommerce.presentation.product;

import java.time.LocalDateTime;
import java.util.List;

import com.study.ecommerce.domain.product.ProductCommand;
import com.study.ecommerce.domain.product.ProductInfo;

public class ProductDto {

	public record PopularRequest(
		LocalDateTime fromOrderDate,
		LocalDateTime toOrderDate,
		Long limit
	){
		public ProductCommand.Search toCommand() {
			return new ProductCommand.Search(
				fromOrderDate,
				toOrderDate,
				limit
			);
		}
	}

	public record AmountResponse(
		Long id,
		String name,
		Long price,
		int amount
	) {
		public static List<ProductDto.AmountResponse> from(
			List<ProductInfo.Amount> products) {
			return products.stream()
				.map(product -> new ProductDto.AmountResponse(
					product.id(),
					product.name(),
					product.price(),
					product.amount()
				)).toList();
		}

		public static ProductDto.AmountResponse from(
			ProductInfo.Amount product) {
			return new ProductDto.AmountResponse(
				product.id(),
				product.name(),
				product.price(),
				product.amount());
		}
	}

	public record OrderAmountResponse(
		Long productId,
		String productName,
		Long productPrice,
		Integer orderAmount
	) {
		public static List<ProductDto.OrderAmountResponse> from(
			List<ProductInfo.OrderAmount> products) {
			return products.stream()
				.map(product -> new ProductDto.OrderAmountResponse(
					product.productId(),
					product.productName(),
					product.productPrice(),
					product.orderAmount()
				)).toList();
		}
	}
}
