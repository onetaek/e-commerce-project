package com.study.ecommerce.infra.product;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.ecommerce.domain.product.ProductInventory;
import com.study.ecommerce.domain.product.QProductInventory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductInventoryQueryDslRepository implements ProductInventoryQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<ProductInventory> getList(Long... productIds) {
		var productInventory = QProductInventory.productInventory;
		return queryFactory.selectFrom(productInventory)
			.where(productInventory.productId.in(productIds))
			.fetch();
	}

	@Override
	public Optional<ProductInventory> getOne(Long productId) {
		var productInventory = QProductInventory.productInventory;
		return Optional.ofNullable(
			queryFactory.selectFrom(productInventory)
				.where(productInventory.productId.eq(productId))
				.fetchOne()
		);
	}
}
