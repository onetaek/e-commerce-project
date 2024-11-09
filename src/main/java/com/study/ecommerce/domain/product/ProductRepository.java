package com.study.ecommerce.domain.product;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository {

	List<Product> getList();

	List<Product> getList(Long... ids);

	List<ProductInventory> getInventoryList(Long... ids);

	List<ProductInfo.OrderAmount> getOrderAmountByRecent3DayAndTop5();

	Product getById(Long productId);

	ProductInventory getInventoryByProductId(Long productId);
}
