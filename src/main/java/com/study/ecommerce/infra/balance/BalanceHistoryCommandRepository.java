package com.study.ecommerce.infra.balance;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.ecommerce.domain.balance.BalanceHistory;

public interface BalanceHistoryCommandRepository extends JpaRepository<BalanceHistory, Long> {
}
