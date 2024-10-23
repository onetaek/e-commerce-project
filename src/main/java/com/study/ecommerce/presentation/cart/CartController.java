package com.study.ecommerce.presentation.cart;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.ecommerce.application.cart.CartAddItemService;
import com.study.ecommerce.application.cart.CartQueryService;
import com.study.ecommerce.application.cart.CartRemoveService;
import com.study.ecommerce.presentation.cart.dto.CartAddRequest;
import com.study.ecommerce.presentation.cart.dto.CartDetailResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CartController {

	private final CartQueryService cartQueryService;
	private final CartAddItemService cartAddItemService;
	private final CartRemoveService cartRemoveService;

	@GetMapping("/api/cart-items")
	public ResponseEntity<CartDetailResponse> getCart(
		@RequestParam String userId
	) {
		cartQueryService.getCart(userId);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/api/cart-items")
	public ResponseEntity<Void> addToCart(
		@RequestBody CartAddRequest request) {
		cartAddItemService.add(request);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/api/cart-items/{id}")
	public ResponseEntity<Void> removeFromCart(
		@PathVariable Long id
	) {
		cartRemoveService.remove(id);
		return ResponseEntity.ok().build();
	}
}
