package com.study.ecommerce.unit.product;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.study.ecommerce.domain.product.Product;
import com.study.ecommerce.domain.product.ProductInventory;
import com.study.ecommerce.domain.product.dto.ProductDetailInfo;
import com.study.ecommerce.domain.product.service.ProductReader;
import com.study.ecommerce.infra.product.ProductInventoryQueryRepository;
import com.study.ecommerce.infra.product.ProductQueryRepository;

@ExtendWith(MockitoExtension.class)
class ProductReaderTest {

	@InjectMocks
	private ProductReader productReader;

	@Mock
	private ProductQueryRepository productQueryRepository;

	@Mock
	private ProductInventoryQueryRepository productInventoryQueryRepository;

	@Test
	@DisplayName("상품 목록과 재고 정보를 결합하여 ProductDetailInfo 리스트를 생성할 수 있어야 한다.")
	void testGetDetailList_Success() {
		// given
		List<Product> productList = List.of(
			Product.builder().id(1L).name("Product A").price(1000L).build(),
			Product.builder().id(2L).name("Product B").price(2000L).build()
		);

		List<ProductInventory> productInventoryList = List.of(
			ProductInventory.builder().productId(1L).amount(10).build(),
			ProductInventory.builder().productId(2L).amount(20).build()
		);

		when(productQueryRepository.getList()).thenReturn(productList);
		when(productInventoryQueryRepository.getList(1L, 2L)).thenReturn(productInventoryList);

		// when
		List<ProductDetailInfo> result = productReader.getDetailList();

		// then
		assertThat(result).hasSize(2);
		assertThat(result).extracting(ProductDetailInfo::id, ProductDetailInfo::name, ProductDetailInfo::price,
				ProductDetailInfo::amount)
			.containsExactlyInAnyOrder(
				tuple(1L, "Product A", 1000L, 10),
				tuple(2L, "Product B", 2000L, 20)
			);

		verify(productQueryRepository, times(1)).getList();
		verify(productInventoryQueryRepository, times(1)).getList(1L, 2L);
	}

	@Test
	@DisplayName("재고 정보가 없는 상품은 재고 수량이 0으로 처리되어야 한다.")
	void testGetDetailList_NoInventory() {
		// given
		List<Product> productList = List.of(
			Product.builder().id(1L).name("Product A").price(1000L).build(),
			Product.builder().id(2L).name("Product B").price(2000L).build()
		);

		// 빈 재고 목록
		List<ProductInventory> productInventoryList = List.of();

		when(productQueryRepository.getList()).thenReturn(productList);
		when(productInventoryQueryRepository.getList(1L, 2L)).thenReturn(productInventoryList);

		// when
		List<ProductDetailInfo> result = productReader.getDetailList();

		// then
		assertThat(result).hasSize(2);
		assertThat(result).extracting(ProductDetailInfo::id, ProductDetailInfo::name, ProductDetailInfo::price,
				ProductDetailInfo::amount)
			.containsExactlyInAnyOrder(
				tuple(1L, "Product A", 1000L, 0),
				tuple(2L, "Product B", 2000L, 0)
			);

		verify(productQueryRepository, times(1)).getList();
		verify(productInventoryQueryRepository, times(1)).getList(1L, 2L);
	}
}