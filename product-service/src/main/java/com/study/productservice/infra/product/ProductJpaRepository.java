package com.study.productservice.infra.product;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.productservice.domain.product.Product;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {
}
