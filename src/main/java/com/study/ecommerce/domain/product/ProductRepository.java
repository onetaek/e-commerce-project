package com.study.ecommerce.domain.product;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository {
	List<Product> getProducts();

	List<Product> getPopularProducts();
}
