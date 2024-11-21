package com.study.productservice.presentation.product;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.productservice.application.ProductFacade;
import com.study.productservice.domain.eventbox.OutboxService;
import com.study.productservice.domain.product.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

	private final ProductFacade productFacade;
	private final ProductService productService;
	private final OutboxService outboxService;

	/**
	 * 상품상세정보를 조회 한다.
	 * 캐시 api 와 분리한 이유는 캐시를 사용하지 않는 경우에도 사용할 수 있도록 하기 위함
	 *
	 * ex) 주문 서비스에서 상품 상세정보를 조회할 때 캐시를 사용하지 않고 가져온다.
	 *
	 * @return 상품상세정보 목록
	 */
	@GetMapping
	public ResponseEntity<List<ProductDto.AmountResponse>> getProductList(
		@RequestParam("ids") Long[] ids
	) {
		return ResponseEntity.ok().body(
			ProductDto.AmountResponse.from(productService.getDetailList(ids))
		);
	}

	/**
	 * 캐싱된 상품상세정보(재고정보도 포함) 목록을 조회한다.
	 *
	 * @return 상품상세정보 목록
	 */
	@GetMapping("cached")
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

	@PostMapping("retry")
	public ResponseEntity<Integer> order() {
		return ResponseEntity.ok().body(
			outboxService.retryFailedOutboxes()
		);
	}
}