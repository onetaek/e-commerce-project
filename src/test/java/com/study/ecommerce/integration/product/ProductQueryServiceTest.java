package com.study.ecommerce.integration.product;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.study.ecommerce.domain.product.Product;
import com.study.ecommerce.domain.product.ProductInventory;
import com.study.ecommerce.domain.product.dto.ProductDetailInfo;
import com.study.ecommerce.domain.product.service.ProductQueryService;

import jakarta.persistence.EntityManager;

@SpringBootTest
@Transactional
class ProductQueryServiceTest {

	@Autowired
	private ProductQueryService productQueryService;

	@Autowired
	private EntityManager entityManager;

	@BeforeEach
	void setUp() {
		// 테스트 데이터 생성
		Product product1 = Product.builder()
			.name("Product A")
			.price(1000L)
			.build();
		Product product2 = Product.builder()
			.name("Product B")
			.price(2000L)
			.build();

		entityManager.persist(product1);
		entityManager.persist(product2);

		ProductInventory inventory1 = ProductInventory.builder()
			.productId(product1.getId())
			.amount(10)
			.build();
		ProductInventory inventory2 = ProductInventory.builder()
			.productId(product2.getId())
			.amount(20)
			.build();

		entityManager.persist(inventory1);
		entityManager.persist(inventory2);

		// 영속성 컨텍스트를 비워서 테스트 중 쿼리를 확인할 수 있게 만듬
		entityManager.flush();
		entityManager.clear();
	}

	@Test
	@DisplayName("상품 목록과 재고 정보를 정상적으로 조회할 수 있어야 한다.")
	void testGetDetailList_Success() {
		// when
		List<ProductDetailInfo> result = productQueryService.getDetailList();

		// then
		assertThat(result).hasSize(2);
		assertThat(result).extracting(ProductDetailInfo::name, ProductDetailInfo::amount)
			.containsExactlyInAnyOrder(
				tuple("Product A", 10),
				tuple("Product B", 20)
			);
	}

	@Test
	@DisplayName("재고 정보가 없는 상품은 재고 수량이 0으로 처리되어야 한다.")
	void testGetDetailList_NoInventory() {
		// given
		Product productWithoutInventory = Product.builder()
			.name("Product C")
			.price(3000L)
			.build();
		entityManager.persist(productWithoutInventory);
		entityManager.flush();
		entityManager.clear();

		// when
		List<ProductDetailInfo> result = productQueryService.getDetailList();

		// then
		assertThat(result).hasSize(3);
		assertThat(result).extracting(ProductDetailInfo::name, ProductDetailInfo::amount)
			.containsExactlyInAnyOrder(
				tuple("Product A", 10),
				tuple("Product B", 20),
				tuple("Product C", 0)
			);
	}
}