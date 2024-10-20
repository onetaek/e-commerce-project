package com.study.ecommerce.infra.balance;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.ecommerce.domain.balance.Balance;
import com.study.ecommerce.domain.balance.QBalance;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BalanceQueryDslRepository implements BalanceQueryRepository {

	private final JPAQueryFactory queryFactory;

	public Optional<Balance> getOne(String userId) {
		var balance = QBalance.balance;

		return Optional.ofNullable(
			queryFactory.selectFrom(balance)
				.where(balance.userId.eq(userId))
				.fetchOne()
		);
	}
}
