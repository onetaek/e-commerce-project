package com.study.productservice.infra.eventbox;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.productservice.domain.eventbox.CommonEventBox;
import com.study.productservice.domain.eventbox.Outbox;
import com.study.productservice.domain.eventbox.OutboxCommand;
import com.study.productservice.domain.eventbox.OutboxRepository;
import com.study.productservice.domain.eventbox.QOutbox;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OutboxRepositoryImpl implements OutboxRepository {

	private final OutboxJpaRepository outboxJpaRepository;
	private final JPAQueryFactory queryFactory;

	@Override
	public void save(Outbox outbox) {
		outboxJpaRepository.save(outbox);
	}

	@Override
	public void update(OutboxCommand.Update command) {
		QOutbox qOutbox = QOutbox.outbox;

		queryFactory.update(qOutbox)
			.set(qOutbox.status, command.status())
			.set(qOutbox.processedAt, command.processedAt())
			.where(
				qOutbox.transactionKey.eq(command.where().transactionKey()),
				qOutbox.status.eq(command.where().status()),
				qOutbox.eventType.eq(command.where().eventType())
			)
			.execute();
	}

	@Override
	public List<Outbox> findAllByStatus(CommonEventBox.Status... status) {
		QOutbox qOutbox = QOutbox.outbox;
		return queryFactory.selectFrom(qOutbox)
			.where(qOutbox.status.in(status))
			.fetch();
	}
}
