package com.study.userservice.infra.eventbox;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.userservice.domain.eventbox.Outbox;

public interface OutboxJpaRepository extends JpaRepository<Outbox, Long> {
}
