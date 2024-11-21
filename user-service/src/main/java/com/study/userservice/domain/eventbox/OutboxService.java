package com.study.userservice.domain.eventbox;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.userservice.domain.point.PointEventHandler;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OutboxService {

	private final OutboxRepository outboxRepository;
	private final PointEventHandler pointEventHandler;

	public void record(OutboxCommand.Create command) {
		outboxRepository.save(command.toOutbox());
	}

	public void update(OutboxCommand.Update command) {
		outboxRepository.update(command);
	}

	public int retryPendingOutboxes() {
		List<Outbox> outboxes = outboxRepository.findAllByStatus(CommonEventBox.Status.PENDING);

		for (Outbox outbox : outboxes) {
			if (outbox.isMaxRetryCount()) {
				outbox.failure();
				continue;
			}
			pointEventHandler.send(
				outbox.getTopic(),
				outbox.getTransactionKey(),
				outbox.getPayload()
			);
			outbox.plusRetryCount();
		}

		return outboxes.size();
	}

	public int retryFailedOutboxes() {
		List<Outbox> outboxes = outboxRepository.findAllByStatus(CommonEventBox.Status.FAILED);

		outboxes.forEach(outbox -> {
			pointEventHandler.send(
				outbox.getTopic(),
				outbox.getTransactionKey(),
				outbox.getPayload()
			);
		});

		return outboxes.size();
	}
}
