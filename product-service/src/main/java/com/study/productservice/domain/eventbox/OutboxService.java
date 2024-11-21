package com.study.productservice.domain.eventbox;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.productservice.domain.product.ProductEventHandler;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OutboxService {

	private final OutboxRepository outboxRepository;
	private final ProductEventHandler productEventHandler;

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
			productEventHandler.send(
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
			productEventHandler.send(
				outbox.getTopic(),
				outbox.getTransactionKey(),
				outbox.getPayload()
			);
		});

		return outboxes.size();
	}
}
