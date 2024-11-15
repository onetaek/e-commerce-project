package com.study.productservice.infra.product;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.productservice.domain.product.ProductInventory;

public interface ProductInventoryJpaRepository extends JpaRepository<ProductInventory, Long> {
}
