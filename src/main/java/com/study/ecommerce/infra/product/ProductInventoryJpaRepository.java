package com.study.ecommerce.infra.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.study.ecommerce.domain.product.ProductInventory;

import jakarta.persistence.LockModeType;

public interface ProductInventoryJpaRepository extends JpaRepository<ProductInventory, Long> {
}
