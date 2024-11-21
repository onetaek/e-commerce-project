package com.study.productservice.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import com.study.productservice.common.annotation.Facade;
import com.study.productservice.common.constant.CacheConstants;
import com.study.productservice.domain.product.ProductCommand;
import com.study.productservice.domain.product.ProductEventCommand;
import com.study.productservice.domain.product.ProductEventService;
import com.study.productservice.domain.product.ProductInfo;
import com.study.productservice.domain.product.ProductService;

import lombok.RequiredArgsConstructor;

@Facade
@Transactional
@RequiredArgsConstructor
public class ProductFacade {
	private final ProductService productService;
	private final ProductEventService productEventService;

	@Cacheable(value = CacheConstants.PRODUCTS_CACHE, key = CacheConstants.PRODUCTS_ALL)
	public List<ProductInfo.Amount> getDetailList(Long[] ids) {
		return productService.getDetailList(ids);
	}

	public void deduct(ProductCommand.Deduct command, String transactionKey) {
		// 상품 재고 차감
		productService.deduct(command);

		// 총 가격 계산
		Map<Long, ProductInfo.Amount> productMap = productService.getDetailList(
			command.products().stream().map(ProductCommand.Deduct.Product::productId).toArray(Long[]::new)
		).stream().collect(
			Collectors.toMap(ProductInfo.Amount::id, product -> product)
		);
		long totalPrice = command.products()
			.stream()
			.mapToLong(product -> product.amount() * productMap.get(product.productId()).price())
			.sum();

		// 이벤트 발행
		productEventService.publishEvent(
			ProductEventCommand.InventoryDeduct.from(
				command, transactionKey, totalPrice
			)
		);
	}

	public void recovery(
		ProductCommand.InventoryRecovery command,
		String transactionKey,
		String errorMessage
	) {
		// 상품 재고 원복
		productService.recovery(command);

		// 이벤트 발행
		productEventService.publishEvent(
			new ProductEventCommand.InventoryDeductFailure(
				transactionKey,
				errorMessage,
				command.orderId(),
				command.userId()
			)
		);
	}
}
