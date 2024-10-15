package com.study.ecommerce.infra.balance;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.study.ecommerce.domain.balance.Balance;

@Repository
public interface BalanceQueryRepository {
	Optional<Balance> getOne(String userId);
}
