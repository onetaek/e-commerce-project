package com.study.ecommerce.infra.balance;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.ecommerce.domain.balance.Balance;

public interface BalanceCommandRepository extends JpaRepository<Balance, String> {
}
