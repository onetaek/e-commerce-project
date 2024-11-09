package com.study.ecommerce.presentation.product;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.study.ecommerce.common.constant.CacheConstants;
import com.study.ecommerce.domain.product.Product;
import com.study.ecommerce.domain.product.ProductInfo;
import com.study.ecommerce.domain.product.ProductInventory;
import com.study.ecommerce.domain.product.ProductRepository;

import lombok.RequiredArgsConstructor;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class ProductSchedule {

	private final ProductRepository productRepository;
	private final CacheManager cacheManager;

	/**
	 * 인기상품 조회 캐시 업데이트
	 * 20분 간격으로 실행 (20분 = 1200000ms)
	 */
	@Scheduled(fixedRate = 1200000)
	public void updatePopularProductsCache() {
		// 새로운 데이터를 DB에서 조회
		List<ProductInfo.OrderAmount> updatedData = productRepository.getOrderAmountByRecent3DayAndTop5();

		// 캐시 덮어쓰기
		Objects.requireNonNull(cacheManager.getCache(CacheConstants.POPULAR_PRODUCTS_CACHE))
			.put(CacheConstants.RECENT_3_DAY_TOP_5_KEY.replace("'", ""), updatedData);
	}

	/**
	 * 상품목록 조회 캐시 업데이트
	 * 3분 간격으로 실행 (3분 = 180000ms)
	 */
	@Scheduled(fixedRate = 180000)
	public void updateProductsCache() {
		// 새로운 데이터를 DB에서 조회
		List<Product> productList = productRepository.getList();
		Long[] productIds = productList.stream().map(Product::getId).toArray(Long[]::new);
		List<ProductInventory> productInventoryList = productRepository.getInventoryList(productIds);
		Map<Long, ProductInventory> inventoryMap = productInventoryList.stream()
			.collect(Collectors.toMap(ProductInventory::getProductId, inventory -> inventory));
		List<ProductInfo.Amount> updatedData = productList.stream()
			.map(product -> {
				ProductInventory inventory = inventoryMap.get(product.getId());
				return new ProductInfo.Amount(
					product.getId(),
					product.getName(),
					product.getPrice(),
					inventory != null ? inventory.getAmount() : 0
				);
			}).collect(Collectors.toList());

		// 캐시 덮어쓰기
		Objects.requireNonNull(cacheManager.getCache(CacheConstants.PRODUCTS_CACHE))
			.put(CacheConstants.PRODUCTS_ALL.replace("'", ""), updatedData);
	}

}
