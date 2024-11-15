package com.study.userservice.presentation.cart;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.userservice.domain.cart.CartCommand;
import com.study.userservice.domain.cart.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;

	@GetMapping("/api/carts")
	public ResponseEntity<List<com.study.userservice.presentation.cart.CartDto.Response>> getCart(
		@RequestParam String userId
	) {
		var carts = cartService.getList(new CartCommand.Search(userId));
		return ResponseEntity.ok().body(com.study.userservice.presentation.cart.CartDto.Response.from(carts));
	}

	@PostMapping("/api/carts")
	public ResponseEntity<Void> addToCart(
		@RequestBody com.study.userservice.presentation.cart.CartDto.AddRequest request) {
		cartService.add(request.toCommand());
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/api/carts/{id}")
	public ResponseEntity<Void> removeFromCart(
		@PathVariable Long id
	) {
		cartService.remove(
			new CartCommand.Remove(id)
		);
		return ResponseEntity.ok().build();
	}
}
