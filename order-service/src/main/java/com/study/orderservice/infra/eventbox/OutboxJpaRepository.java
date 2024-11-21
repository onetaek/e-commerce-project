package com.study.orderservice.infra.eventbox;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.orderservice.domain.eventbox.Outbox;

public interface OutboxJpaRepository extends JpaRepository<Outbox, Long> {
}
