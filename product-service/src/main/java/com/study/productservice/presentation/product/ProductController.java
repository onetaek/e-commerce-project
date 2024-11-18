package com.study.productservice.presentation.product;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.productservice.application.ProductFacade;
import com.study.productservice.domain.product.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

	private final ProductFacade productFacade;
	private final ProductService productService;

	@GetMapping
	public ResponseEntity<List<ProductDto.AmountResponse>> getProductList(
		@RequestParam("ids") Long[] ids
	) {
		return ResponseEntity.ok().body(
			ProductDto.AmountResponse.from(productService.getDetailList(ids))
		);
	}

	/**
	 * 상품상세정보(재고정보도 포함) 목록을 조회 한다.
	 *
	 * @return 상품상세정보 목록
	 */
	@GetMapping("detail")
	public ResponseEntity<List<ProductDto.AmountResponse>> getProductDetailList(
		@RequestParam("ids") Long[] ids
	) {
		return ResponseEntity.ok().body(
			ProductDto.AmountResponse.from(productFacade.getDetailList(ids))
		);
	}

	/**
	 * 상품상세정보를 조회 한다.
	 *
	 * @return 상품상세정보
	 */
	@GetMapping("{id}")
	public ResponseEntity<ProductDto.AmountResponse> getProduct(
		@PathVariable(name = "id") Long id
	) {
		return ResponseEntity.ok().body(
			ProductDto.AmountResponse.from(productService.getDetail(id))
		);
	}
}