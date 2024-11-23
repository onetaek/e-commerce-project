package com.study.userservice.presentation.point;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.study.userservice.common.exception.SendAlertException;
import com.study.userservice.common.utility.JacksonUtils;
import com.study.userservice.domain.eventbox.CommonEventBox;
import com.study.userservice.domain.eventbox.Outbox;
import com.study.userservice.domain.eventbox.OutboxCommand;
import com.study.userservice.domain.eventbox.OutboxService;
import com.study.userservice.domain.point.PointEventCommand;
import com.study.userservice.domain.point.PointEventService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PointEventListener {

	@Value("${application.topic.user.point-use}")
	private String userPointUseTopic;

	@Value("${application.topic.user.point-use-failure}")
	private String userPointUseFailureTopic;

	private final OutboxService outboxService;
	private final PointEventService pointEventService;

	@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
	public void recordOutbox(PointEventCommand.Use event) {
		outboxService.record(
			new OutboxCommand.Create(
				event.transactionKey(),
				CommonEventBox.Status.PENDING,
				Outbox.EventType.USE_POINT,
				userPointUseTopic,
				JacksonUtils.convertObjectToJsonString(event)
			)
		);
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void sendOrderCreateEvent(PointEventCommand.Use event) {
		try {
			pointEventService.send(
				userPointUseTopic,
				event.transactionKey(),
				JacksonUtils.convertObjectToJsonString(event)
			);
		} catch (Exception e) {
			throw new SendAlertException("포인트 사용 이벤트 메시지 전송 실패");
		}
	}

	@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
	public void recordOutbox(PointEventCommand.UseFailure event) {
		outboxService.record(
			new OutboxCommand.Create(
				event.transactionKey(),
				CommonEventBox.Status.PENDING,
				Outbox.EventType.USE_POINT_FAIlURE,
				userPointUseFailureTopic,
				JacksonUtils.convertObjectToJsonString(event)
			)
		);
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void sendOrderCreateEvent(PointEventCommand.UseFailure event) {
		try {
			pointEventService.send(
				userPointUseFailureTopic,
				event.transactionKey(),
				JacksonUtils.convertObjectToJsonString(event)
			);
		} catch (Exception e) {
			throw new SendAlertException("포인트 사용 실패 이벤트 메시지 전송 실패");
		}
	}
}
