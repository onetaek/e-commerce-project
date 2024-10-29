package com.study.ecommerce.infra.point;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.ecommerce.domain.point.PointHistory;

public interface PointHistoryJpaRepository extends JpaRepository<PointHistory, Long> {
}
