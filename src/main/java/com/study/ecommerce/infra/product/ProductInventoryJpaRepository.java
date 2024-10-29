package com.study.ecommerce.infra.product;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.ecommerce.domain.product.ProductInventory;

public interface ProductInventoryJpaRepository extends JpaRepository<ProductInventory, Long> {
}
