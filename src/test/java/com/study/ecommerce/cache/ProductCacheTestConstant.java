package com.study.ecommerce.cache;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;

import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.cache.RedisCacheManager;

import com.study.ecommerce.application.ProductFacade;
import com.study.ecommerce.common.constant.CacheConstants;
import com.study.ecommerce.domain.product.ProductRepository;

@EnableCaching
@SpringBootTest
public class ProductCacheTestConstant {
	@SpyBean
	private ProductRepository productRepository;

	@Autowired
	private ProductFacade productFacade;

	@Autowired
	private RedisCacheManager redisCacheManager;

	@BeforeEach
	public void setUp() {
		openMocks(this);
	}

	@Test
	@DisplayName("인기상품조회: 캐시가 비워져 있는 상황에서 2번 호출을 하면 DB호출은 1번만 발생한다.")
	public void testGetPopularProductsCaching() {
		// given
		Objects.requireNonNull(redisCacheManager.getCache(CacheConstants.POPULAR_PRODUCTS_CACHE)).clear();

		// when
		for (int i = 0 ; i < 2; i++) {
			productFacade.getPopularProducts();
		}

		// then
		verify(productRepository, times(1)).getOrderAmountByRecent3DayAndTop5();
	}

	@Test
	@DisplayName("상품목록조회: 캐시가 비워져 있는 상황에서 2번 호출을 하면 DB호출은 1번만 발생한다.")
	public void testGetProductsCaching() {
		// given
		Objects.requireNonNull(redisCacheManager.getCache(CacheConstants.PRODUCTS_CACHE)).clear();

		// when
		for (int i = 0 ; i < 5; i++) {
			productFacade.getDetailList();
		}

		// then
		verify(productRepository, times(1)).getList();
	}
}
