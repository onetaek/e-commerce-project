package com.study.ecommerce.infra.product;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.study.ecommerce.domain.product.Product;

@Repository
public interface ProductQueryRepository {
	List<Product> getList();

	Optional<Product> getOne(Long id);

	List<Product> getPopularProducts();

}
