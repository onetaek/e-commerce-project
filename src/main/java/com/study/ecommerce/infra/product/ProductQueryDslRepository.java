package com.study.ecommerce.infra.product;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.ecommerce.domain.product.Product;
import com.study.ecommerce.domain.product.QProduct;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductQueryDslRepository implements ProductQueryRepository {

	private final JPAQueryFactory queryFactory;

	public List<Product> getList() {
		var qProduct = QProduct.product;
		return queryFactory.selectFrom(qProduct).fetch();
	}

	@Override
	public Optional<Product> getOne(Long id) {
		var qProduct = QProduct.product;
		return Optional.ofNullable(
			queryFactory.selectFrom(qProduct)
				.where(qProduct.id.eq(id))
				.fetchOne()
		);
	}

	public List<Product> getPopularProducts() {
		return null;
	}

}
