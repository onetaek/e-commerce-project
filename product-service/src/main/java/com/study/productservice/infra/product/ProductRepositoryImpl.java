package com.study.productservice.infra.product;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.productservice.domain.product.Product;
import com.study.productservice.domain.product.ProductInventory;
import com.study.productservice.domain.product.ProductRepository;
import com.study.productservice.domain.product.QProduct;
import com.study.productservice.domain.product.QProductInventory;

import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Product> getList(Long... ids) {
		var qProduct = QProduct.product;
		return queryFactory.selectFrom(qProduct)
			.where(ids == null ? null : qProduct.id.in(ids))
			.fetch();
	}

	@Override
	public List<ProductInventory> getInventoryListWithLock(Long... productIds) {
		var productInventory = QProductInventory.productInventory;
		return queryFactory.selectFrom(productInventory)
			.where(productInventory.productId.in(productIds))
			.setLockMode(LockModeType.PESSIMISTIC_WRITE)  // 비관적 읽기 락 설정
			.fetch();
	}

	@Override
	public Product getById(Long productId) {
		var qProduct = QProduct.product;
		return queryFactory.selectFrom(qProduct)
			.where(qProduct.id.eq(productId))
			.fetchOne();
	}

	@Override
	public ProductInventory getInventoryByProductId(Long productId) {
		var qProductInventory = QProductInventory.productInventory;
		return queryFactory.selectFrom(qProductInventory)
			.where(qProductInventory.productId.eq(productId))
			.fetchOne();
	}
}
