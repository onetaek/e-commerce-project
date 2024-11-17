package com.study.orderservice.presentation;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KafkaTestConsumer {

	@KafkaListener(topics = "#{@environment.getProperty('application.topic.topic_test_1')}")
	public void consumer1(ConsumerRecord<String, String> consumerRecord) {
		String key = consumerRecord.key();
		System.out.println("key = " + key);

		String value = consumerRecord.value();
		System.out.println("value = " + value);
	}

	@KafkaListener(topics = "#{@environment.getProperty('application.topic.topic_test_2')}")
	public void consumer2(ConsumerRecord<String, String> consumerRecord) {
		String key = consumerRecord.key();
		System.out.println("key = " + key);

		String value = consumerRecord.value();
		System.out.println("value = " + value);
	}
}
