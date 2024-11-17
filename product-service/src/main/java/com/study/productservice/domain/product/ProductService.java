package com.study.productservice.domain.product;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	/**
	 * <h1>제품 정보 상세목록 조회</h1>
	 * <ul>
	 *     <li>코드는 복잡해보이지만 별거 없다.</li>
	 *     <li>1대1 관계로 Product 와 ProductInventory 가 있는데 이를 매핑 시켜줄 뿐이다.</li>
	 * </ul>
	 */
	@Transactional
	public List<ProductInfo.Amount> getDetailList(Long[] ids) {
		List<Product> productList = productRepository.getList(ids);
		Long[] productIds = productList.stream().map(Product::getId).toArray(Long[]::new);
		List<ProductInventory> productInventoryList = productRepository.getInventoryListWithLock(productIds);

		Map<Long, ProductInventory> inventoryMap = productInventoryList.stream()
			.collect(Collectors.toMap(ProductInventory::getProductId, inventory -> inventory));

		return productList.stream()
			.map(product -> {
				ProductInventory inventory = inventoryMap.get(product.getId());
				return new ProductInfo.Amount(
					product.getId(),
					product.getName(),
					product.getPrice(),
					inventory != null ? inventory.getAmount() : 0
				);
			})
			.collect(Collectors.toList());
	}

	/**
	 * <h1>제품 정보 단건조회</h1>
	 */
	@Transactional(readOnly = true)
	public ProductInfo.Amount getDetail(Long productId) {
		Product product = productRepository.getById(productId);
		ProductInventory productInventory = productRepository.getInventoryByProductId(productId);
		return new ProductInfo.Amount(
			product.getId(),
			product.getName(),
			product.getPrice(),
			productInventory != null ? productInventory.getAmount() : 0
		);
	}

	/**
	 * <h1>상위 상품 조회</h1>
	 * <ul>
	 *     <li>쿼리를 통해 group by 와 limit 를 사용해서 상위 주문 수량을 집계하였다.</li>
	 * </ul>
	 */
	@Transactional(readOnly = true)
	public List<ProductInfo.OrderAmount> getPopularProducts(
		ProductCommand.Search command
	) {
		return productRepository.getOrderAmountByOrderDateAndLimit(command);
	}

	/**
	 * <h1>상품 재고 차감</h1>
	 */
	public void deduct(ProductCommand.Deduct command) {
		var productIds = command.products().stream().map(ProductCommand.Deduct.Product::productId).toArray(Long[]::new);
		var inventoryList = productRepository.getInventoryListWithLock(productIds);
		var commandProducts = command.products().stream()
			.collect(Collectors.toMap(ProductCommand.Deduct.Product::productId, Function.identity()));

		for (ProductInventory inventory : inventoryList) {
			var commandProduct = commandProducts.get(inventory.getProductId());
			inventory.subtract(commandProduct.amount());
		}
	}
}
