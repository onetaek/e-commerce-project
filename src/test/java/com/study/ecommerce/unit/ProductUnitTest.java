package com.study.ecommerce.unit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.study.ecommerce.domain.product.ProductException;
import com.study.ecommerce.domain.product.ProductInventory;

public class ProductUnitTest {

	private ProductInventory productInventory;

	@BeforeEach
	public void setUp() {
		productInventory = ProductInventory.builder()
			.id(1L)
			.productId(100L)
			.amount(50)
			.build();
	}

	@Test
	@DisplayName("상품 재고 감소 - 주어진 금액만큼 재고가 감소해야 한다")
	public void testSubtractWithinInventory() {
		int subtractAmount = 20;
		productInventory.subtract(subtractAmount);
		assertEquals(30, productInventory.getAmount());
	}

	@Test
	@DisplayName("상품 재고 감소 - 감소하려는 금액이 재고를 초과할 경우 예외가 발생해야 한다")
	public void testSubtractExceedingInventory() {
		int subtractAmount = 60;
		ProductException exception = assertThrows(ProductException.class, () -> {
			productInventory.subtract(subtractAmount);
		});
		assertEquals(ProductException.inventoryAmountExceed().getMessage(), exception.getMessage());
	}
}
