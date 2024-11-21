package com.study.productservice.infra.eventbox;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.productservice.domain.eventbox.Outbox;

public interface OutboxJpaRepository extends JpaRepository<Outbox, Long> {
}
