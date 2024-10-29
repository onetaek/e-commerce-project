package com.study.ecommerce.presentation.product;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.ecommerce.domain.product.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

	private final ProductService productService;

	/**
	 * 상품상세정보(재고정보도 포함) 목록을 조회 한다.
	 * @return 상품상세정보 목록
	 */
	@GetMapping
	public ResponseEntity<List<ProductDto.AmountResponse>> getProducts() {
		return ResponseEntity.ok().body(
			ProductDto.AmountResponse.from(productService.getDetailList())
		);
	}

	/**
	 * 상위 5개 주문
	 * @return 상품주문수량정보 목록
	 */
	@GetMapping("popular")
	public ResponseEntity<List<ProductDto.OrderAmountResponse>> getPopularProducts() {
		return ResponseEntity.ok().body(
			ProductDto.OrderAmountResponse.from(productService.getPopularProducts())
		);
	}
}