package com.study.orderservice.common.config;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import com.study.orderservice.common.constant.KafkaCustomProperties;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {
	private final KafkaCustomProperties kafkaProperties;

	public static final String KAFKA_LISTENER_FACTORY_GROUP_LOGIC = "kafkaListenerContainerFactoryGroupLogic";
	public static final String KAFKA_LISTENER_FACTORY_GROUP_OUTBOX = "kafkaListenerContainerFactoryGroupOutbox";

	@Bean
	public ConsumerFactory<String, String> consumerFactoryGroupLogic() {
		Map<String, Object> properties = Map.of(
			ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers(),
			ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getConsumerGroupIdLogic(),
			ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getKeyDeserializer(),
			ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getValueDeserializer()
		);
		return new DefaultKafkaConsumerFactory<>(properties);
	}

	@Bean(name = KAFKA_LISTENER_FACTORY_GROUP_LOGIC)
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactoryGroupLogic(
		ConsumerFactory<String, String> consumerFactoryGroupLogic) {
		ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory
			= new ConcurrentKafkaListenerContainerFactory<>();
		kafkaListenerContainerFactory.setConsumerFactory(consumerFactoryGroupLogic);
		return kafkaListenerContainerFactory;
	}

	@Bean
	public ConsumerFactory<String, String> consumerFactoryGroupOutbox() {
		Map<String, Object> properties = Map.of(
			ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers(),
			ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getConsumerGroupIdOutbox(),
			ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getKeyDeserializer(),
			ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getValueDeserializer()
		);
		return new DefaultKafkaConsumerFactory<>(properties);
	}

	@Bean(name = KAFKA_LISTENER_FACTORY_GROUP_OUTBOX)
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactoryGroupOutbox(
		ConsumerFactory<String, String> consumerFactoryGroupOutbox) {
		ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory
			= new ConcurrentKafkaListenerContainerFactory<>();
		kafkaListenerContainerFactory.setConsumerFactory(consumerFactoryGroupOutbox);
		return kafkaListenerContainerFactory;
	}
}
