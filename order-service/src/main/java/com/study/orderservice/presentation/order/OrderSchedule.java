package com.study.orderservice.presentation.order;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.study.orderservice.domain.eventbox.OutboxService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class OrderSchedule {

	private final OutboxService outboxService;

	@Scheduled(fixedDelay = 1000 * 60 * 5)
	public void reSendByPendingAndRetryCount() {
		int count = outboxService.retryPendingOutboxes();
		if (count > 0)
			log.info("Re-Send Pending Outboxes Count: {}", count);
	}

}
