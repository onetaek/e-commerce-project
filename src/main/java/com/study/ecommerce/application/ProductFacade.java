package com.study.ecommerce.application;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.study.ecommerce.common.annotation.ApplicationService;
import com.study.ecommerce.common.constant.CacheConstants;
import com.study.ecommerce.domain.product.ProductInfo;
import com.study.ecommerce.domain.product.ProductService;

import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class ProductFacade {
	private final ProductService productService;

	@Cacheable(value = CacheConstants.PRODUCTS_CACHE, key = CacheConstants.PRODUCTS_ALL)
	public List<ProductInfo.Amount> getDetailList() {
		return productService.getDetailList();
	}

	@Cacheable(value = CacheConstants.POPULAR_PRODUCTS_CACHE, key = CacheConstants.RECENT_3_DAY_TOP_5_KEY)
	public List<ProductInfo.OrderAmount> getPopularProducts() {
		return productService.getPopularProducts();
	}
}
