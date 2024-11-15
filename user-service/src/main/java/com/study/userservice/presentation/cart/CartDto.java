package com.study.userservice.presentation.cart;

import java.util.List;

import com.study.userservice.domain.cart.CartCommand;
import com.study.userservice.domain.cart.CartInfo;

public class CartDto {
	public record AddRequest(
		Long productId,
		int amount,
		String userId
	) {
		public CartCommand.Add toCommand() {
			return new CartCommand.Add(
				productId,
				amount,
				userId
			);
		}
	}

	public record Response(
		Long id,
		Long productId,
		int amount,
		String userId
	) {
		public static List<Response> from(List<CartInfo.Info> cartInfos) {
			return cartInfos.stream().map(v ->
				new Response(
					v.id(),
					v.productId(),
					v.amount(),
					v.userId()
				)
			).toList();
		}
	}

}
