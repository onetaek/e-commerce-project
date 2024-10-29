package com.study.ecommerce.domain.product;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	/**
	 * <h1>제품 정보 상세 조회</h1>
	 * <ul>
	 *     <li>코드는 복잡해보이지만 별거 없다.</li>
	 *     <li>1대1 관계로 Product 와 ProductInventory 가 있는데 이를 매핑 시켜줄 뿐이다.</li>
	 * </ul>
	 */
	public List<ProductInfo.Amount> getDetailList() {
		List<Product> productList = productRepository.getList();
		Long[] productIds = productList.stream().map(Product::getId).toArray(Long[]::new);
		List<ProductInventory> productInventoryList = productRepository.getInventoryList(productIds);

		Map<Long, ProductInventory> inventoryMap = productInventoryList.stream()
			.collect(Collectors.toMap(ProductInventory::getProductId, inventory -> inventory));

		return productList.stream()
			.map(product -> {
				ProductInventory inventory = inventoryMap.get(product.getId());
				return new ProductInfo.Amount(
					product.getId(),
					product.getName(),
					product.getPrice(),
					inventory != null ? inventory.getAmount() : 0 // 재고가 없는 경우 0 처리
				);
			})
			.collect(Collectors.toList());
	}

	/**
	 * <h1>상위 상품 조회</h1>
	 * <ul>
	 *     <li>쿼리를 통해 group by 와 limit 를 사용해서 상위 주문 수량을 집계하였다.</li>
	 * </ul>
	 */
	public List<ProductInfo.OrderAmount> getPopularProducts() {
		return productRepository.getOrderAmountByRecent3DayAndTop5();
	}
}
