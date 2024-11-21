package com.study.productservice.domain.eventbox;

import java.util.List;

public interface OutboxRepository {

	void save(Outbox outbox);

	void update(OutboxCommand.Update command);

	List<Outbox> findAllByStatus(CommonEventBox.Status... status);
}
