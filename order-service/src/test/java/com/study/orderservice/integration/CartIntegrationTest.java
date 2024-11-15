package com.study.orderservice.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.study.orderservice.domain.cart.Cart;
import com.study.orderservice.domain.cart.CartCommand;
import com.study.orderservice.domain.cart.CartInfo;
import com.study.orderservice.domain.cart.CartService;
import com.study.orderservice.infra.cart.CartJpaRepository;

@Transactional
@SpringBootTest
public class CartIntegrationTest {

	@Autowired
	private CartService cartService;

	@Autowired
	private CartJpaRepository cartJpaRepository;

	@BeforeEach
	public void setUp() {
		cartJpaRepository.deleteAll();
	}

	@Test
	@DisplayName("장바구니 조회 - 유저 ID를 통해 장바구니 목록을 조회한다")
	public void testGetList() {
		Cart cart = Cart.builder()
			.userId("testUser")
			.productId(1L)
			.amount(2)
			.build();
		cartJpaRepository.save(cart);

		CartCommand.Search command = new CartCommand.Search("testUser");
		List<CartInfo.Info> cartList = cartService.getList(command);

		assertEquals(1, cartList.size());
		assertEquals("testUser", cartList.get(0).userId());
		assertEquals(1L, cartList.get(0).productId());
		assertEquals(2, cartList.get(0).amount());
	}

	@Test
	@DisplayName("장바구니 등록 - 장바구니에 상품을 등록한다")
	public void testAdd() {
		CartCommand.Add command = new CartCommand.Add(1L, 3, "testUser");
		cartService.add(command);

		List<Cart> cartList = cartJpaRepository.findAll();
		assertEquals(1, cartList.size());
		assertEquals("testUser", cartList.get(0).getUserId());
		assertEquals(1L, cartList.get(0).getProductId());
		assertEquals(3, cartList.get(0).getAmount());
	}

	@Test
	@DisplayName("장바구니 삭제 - 장바구니에서 상품을 삭제한다")
	public void testRemove() {
		Cart cart = Cart.builder()
			.userId("testUser")
			.productId(1L)
			.amount(2)
			.build();
		Cart savedCart = cartJpaRepository.save(cart);

		CartCommand.Remove command = new CartCommand.Remove(savedCart.getId());
		cartService.remove(command);

		List<Cart> cartList = cartJpaRepository.findAll();
		assertTrue(cartList.isEmpty());
	}
}
