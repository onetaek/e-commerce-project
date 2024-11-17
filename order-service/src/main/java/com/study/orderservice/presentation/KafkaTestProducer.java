package com.study.orderservice.presentation;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class KafkaTestProducer {

	private final KafkaTemplate<String, Object> kafkaTemplate;

	@GetMapping("kafka/producer")
	public void test1(
		@RequestParam("topic") String topic,
		@RequestParam("key") String key,
		@RequestParam("message") String message
	) {
		kafkaTemplate.send(topic, key, message);
	}
}
