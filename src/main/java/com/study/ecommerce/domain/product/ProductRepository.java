package com.study.ecommerce.domain.product;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository {
	Optional<Product> getInventory(Long id);

	List<Product> getList();

	List<Product> getList(Long... ids);

	List<ProductInventory> getInventoryList(Long... ids);

	List<ProductInfo.OrderAmount> getOrderAmountByRecent3DayAndTop5();

	List<ProductInventory> findByProductIdsForUpdate(Long... productIds);
}
