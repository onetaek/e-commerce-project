package com.study.ecommerce.infra.product;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.study.ecommerce.domain.product.ProductInventory;

@Repository
public interface ProductInventoryQueryRepository {
	List<ProductInventory> getList(Long... productIds);

	Optional<ProductInventory> getOne(Long productId);
}
