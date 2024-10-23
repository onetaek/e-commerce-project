package com.study.ecommerce.presentation.product;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.ecommerce.application.product.ProductQueryService;
import com.study.ecommerce.presentation.product.dto.ProductDetailResponse;
import com.study.ecommerce.presentation.product.dto.ProductOrderAmountResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

	private final ProductQueryService productQueryService;

	/**
	 * 상품상세정보(재고정보도 포함) 목록을 조회 한다.
	 * @return 상품상세정보 목록
	 */
	@GetMapping
	public ResponseEntity<List<ProductDetailResponse>> getProducts() {
		return ResponseEntity.ok().body(
			ProductDetailResponse.from(productQueryService.getDetailList())
		);
	}

	/**
	 * 상위 5개 주문
	 * @return 상품주문수량정보 목록
	 */
	@GetMapping("popular")
	public ResponseEntity<List<ProductOrderAmountResponse>> getPopularProducts() {
		return ResponseEntity.ok().body(
			ProductOrderAmountResponse.from(productQueryService.getPopularProducts())
		);
	}
}
