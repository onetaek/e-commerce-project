package com.study.ecommerce.domain.product.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.ecommerce.domain.product.Product;
import com.study.ecommerce.domain.product.ProductInventory;
import com.study.ecommerce.domain.product.dto.ProductDetailInfo;
import com.study.ecommerce.infra.product.ProductInventoryQueryRepository;
import com.study.ecommerce.infra.product.ProductQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductQueryService {

	private final ProductQueryRepository productQueryRepository;
	private final ProductInventoryQueryRepository productInventoryQueryRepository;

	// TODO 검색 조건 추가
	public List<ProductDetailInfo> getDetailList() {
		List<Product> productList = productQueryRepository.getList();
		Long[] productIds = productList.stream().map(Product::getId).toArray(Long[]::new);
		List<ProductInventory> productInventoryList = productInventoryQueryRepository.getList(productIds);

		Map<Long, ProductInventory> inventoryMap = productInventoryList.stream()
			.collect(Collectors.toMap(ProductInventory::getProductId, inventory -> inventory));

		return productList.stream()
			.map(product -> {
				ProductInventory inventory = inventoryMap.get(product.getId());
				return new ProductDetailInfo(
					product.getId(),
					product.getName(),
					product.getPrice(),
					inventory != null ? inventory.getAmount() : 0 // 재고가 없는 경우 0 처리
				);
			})
			.collect(Collectors.toList());
	}
}
