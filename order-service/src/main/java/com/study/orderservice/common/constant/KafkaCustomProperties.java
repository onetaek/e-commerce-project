package com.study.orderservice.common.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "application.kafka-property")
public class KafkaCustomProperties {

	private String bootstrapServers;

	// Producer
	private String keySerializer;
	private String valueSerializer;

	// Consumer
	private String keyDeserializer;
	private String valueDeserializer;
	private String consumerGroupId;
}
