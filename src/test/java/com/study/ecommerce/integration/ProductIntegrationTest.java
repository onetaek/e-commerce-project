package com.study.ecommerce.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.study.ecommerce.domain.product.Product;
import com.study.ecommerce.domain.product.ProductInfo;
import com.study.ecommerce.domain.product.ProductInventory;
import com.study.ecommerce.domain.product.ProductService;
import com.study.ecommerce.infra.product.ProductInventoryJpaRepository;
import com.study.ecommerce.infra.product.ProductJpaRepository;

@Transactional
@SpringBootTest
public class ProductIntegrationTest {
	@Autowired
	private ProductService productService;

	@Autowired
	private ProductJpaRepository productJpaRepository;

	@Autowired
	private ProductInventoryJpaRepository productInventoryJpaRepository;

	private Product savedProduct1;
	private Product savedProduct2;
	private ProductInventory savedInventory1;
	private ProductInventory savedInventory2;

	@BeforeEach
	public void setUp() {
		Product product1 = Product.builder()
			.name("Product1")
			.price(1000L)
			.build();
		Product product2 = Product.builder()
			.name("Product2")
			.price(2000L)
			.build();
		savedProduct1 = productJpaRepository.save(product1);
		savedProduct2 = productJpaRepository.save(product2);

		ProductInventory inventory1 = ProductInventory.builder()
			.productId(savedProduct1.getId())
			.amount(10)
			.build();
		ProductInventory inventory2 = ProductInventory.builder()
			.productId(savedProduct2.getId())
			.amount(20)
			.build();
		savedInventory1 = productInventoryJpaRepository.save(inventory1);
		savedInventory2 = productInventoryJpaRepository.save(inventory2);
	}

	@Test
	@DisplayName("제품 정보 상세 조회 - 모든 제품과 재고 정보를 조회한다")
	public void testGetDetailList() {
		List<ProductInfo.Amount> detailList = productService.getDetailList();
		assertEquals(2, detailList.size());

		ProductInfo.Amount product1 = detailList.stream()
			.filter(p -> p.id().equals(savedProduct1.getId()))
			.findFirst()
			.orElse(null);
		assertNotNull(product1);
		assertEquals(savedInventory1.getAmount(), product1.amount());

		ProductInfo.Amount product2 = detailList.stream()
			.filter(p -> p.id().equals(savedProduct2.getId()))
			.findFirst()
			.orElse(null);
		assertNotNull(product2);
		assertEquals(savedInventory2.getAmount(), product2.amount());
	}
}
