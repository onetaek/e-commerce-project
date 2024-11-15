package com.study.productservice.application;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.study.productservice.common.annotation.Facade;
import com.study.productservice.common.constant.CacheConstants;
import com.study.productservice.domain.product.ProductCommand;
import com.study.productservice.domain.product.ProductInfo;
import com.study.productservice.domain.product.ProductService;

import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class ProductFacade {
	private final ProductService productService;

	@Cacheable(value = CacheConstants.PRODUCTS_CACHE, key = CacheConstants.PRODUCTS_ALL)
	public List<ProductInfo.Amount> getDetailList() {
		return productService.getDetailList();
	}

	@Cacheable(value = CacheConstants.POPULAR_PRODUCTS_CACHE, key = CacheConstants.RECENT_3_DAY_TOP_5_KEY)
	public List<ProductInfo.OrderAmount> getPopularProducts(
		ProductCommand.Search command
	) {
		return productService.getPopularProducts(command);
	}
}
