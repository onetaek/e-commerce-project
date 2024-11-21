package com.study.userservice.domain.point;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointEventService {

	private final PointEventHandler pointEventHandler;

	@Transactional
	public <T> void publishEvent(T event) {
		pointEventHandler.publish(event);
	}

	public <T> void send(
		String pointUseTopic,
		String key,
		T event
	) {
		pointEventHandler.send(pointUseTopic, key, event);
	}
}
