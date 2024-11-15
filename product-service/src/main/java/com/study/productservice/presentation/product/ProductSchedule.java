package com.study.productservice.presentation.product;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.study.productservice.common.constant.CacheConstants;
import com.study.productservice.domain.product.ProductCommand;
import com.study.productservice.domain.product.ProductService;

import lombok.RequiredArgsConstructor;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class ProductSchedule {

	private final ProductService productService;
	private final CacheManager cacheManager;

	/**
	 * 인기상품 조회 캐시 업데이트
	 * 20분 간격으로 실행 (20분 = 1200000ms)
	 */
	@Scheduled(fixedRate = 1200000)
	public void updatePopularProductsCache() {
		// 새로운 데이터를 DB에서 조회
		var updatedData = productService.getPopularProducts(
			new ProductCommand.Search(
				LocalDateTime.now().minusDays(3),
				LocalDateTime.now(),
				5L
			)
		);

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
		var updatedData = productService.getDetailList();

		// 캐시 덮어쓰기
		Objects.requireNonNull(cacheManager.getCache(CacheConstants.PRODUCTS_CACHE))
			.put(CacheConstants.PRODUCTS_ALL.replace("'", ""), updatedData);
	}

}
