package com.study.userservice.presentation.point;

import java.util.List;

import com.study.userservice.domain.point.PointCommand;
import com.study.userservice.domain.point.PointEventCommand;
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

	public record InventoryDeductMessage(
		String transactionKey,
		long orderId,
		String userId,
		long totalPrice,
		List<Product> products
	) {
		public record Product(
			Long productId,
			int amount,
			long price
		) {
		}

		public PointCommand.Use toCommand() {
			return new PointCommand.Use(userId, orderId, totalPrice);
		}

		public PointEventCommand.UseFailure toUseFailureCommand(String errorMessage) {
			return new PointEventCommand.UseFailure(
				transactionKey,
				errorMessage,
				userId,
				orderId,
				products.stream()
					.map(product -> new PointEventCommand.UseFailure.Product(
						product.productId(),
						product.amount(),
						product.price()
					))
					.toList()
			);
		}
	}

	public record UsePointMessage(
		String transactionKey,
		String userId,
		long orderId,
		long sumPrice
	) {
	}

	public record UsePointFailureMessage(
		String transactionKey,
		String userId,
		long orderId,
		List<Product> products
	) {
		public record Product(
			Long productId,
			int amount,
			long price
		) {
		}
	}

}
