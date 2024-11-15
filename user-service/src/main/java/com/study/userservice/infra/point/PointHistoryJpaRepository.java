package com.study.userservice.infra.point;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.userservice.domain.point.PointHistory;

public interface PointHistoryJpaRepository extends JpaRepository<PointHistory, Long> {
}
