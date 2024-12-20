package com.study.productservice.domain.product;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository {

	List<Product> getList(Long... ids);

	List<ProductInventory> getInventoryListWithLock(Long... ids);

	Product getById(Long productId);

	ProductInventory getInventoryByProductId(Long productId);
}
