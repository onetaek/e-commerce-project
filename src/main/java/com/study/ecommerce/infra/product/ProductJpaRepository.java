package com.study.ecommerce.infra.product;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.ecommerce.domain.product.Product;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {
}
