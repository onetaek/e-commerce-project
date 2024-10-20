package com.study.ecommerce.presentation.product;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.ecommerce.domain.product.service.ProductQueryService;
import com.study.ecommerce.presentation.product.dto.ProductDetailResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

	private final ProductQueryService productQueryService;

	@GetMapping
	public ResponseEntity<List<ProductDetailResponse>> getProducts() {
		return ResponseEntity.ok().body(
			ProductDetailResponse.from(productQueryService.getDetailList())
		);
	}

	/**
	 * 최근 3일간 가장 많이 팔린 상위 5개 상품 정보를 제공하는 API 를 작성합니다.
	 * 통계 정보를 다루기 위한 기술적 고민을 충분히 해보도록 합니다.
	 */
	// @GetMapping("popular")
	// public List<ProductResponse> getPopularProducts() {
	// 	return productFacade.getPopularProducts();
	// }
}
