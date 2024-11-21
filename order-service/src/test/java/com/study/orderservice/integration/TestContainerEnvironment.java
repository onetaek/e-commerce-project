package com.study.orderservice.integration;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import com.study.orderservice.infra.eventbox.OutboxJpaRepository;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@RecordApplicationEvents
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public abstract class TestContainerEnvironment {
	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	private ApplicationEvents applicationEvents;

	@Autowired
	OutboxJpaRepository outboxJpaRepository;

	@AfterEach
	void tearDown() {
		applicationEvents.clear();
		outboxJpaRepository.deleteAll();
	}

	// MySQL 시작
	@Container
	public static final String MY_SQL_FULL_IMAGE_NAME = "mysql:8.0.36";
	private static final int MY_SQL_PORT = 3306;
	private static final MySQLContainer<?> MY_SQL_CONTAINER;

	static {
		try (MySQLContainer<?> mySQLContainer = new MySQLContainer<>(DockerImageName.parse(MY_SQL_FULL_IMAGE_NAME))) {
			MY_SQL_CONTAINER = mySQLContainer
				.withExposedPorts(MY_SQL_PORT)
				.withEnv("MYSQL_ROOT_PASSWORD", "password")
				.withReuse(true);
			MY_SQL_CONTAINER.start();
		}
	}
	// MySQL 끝

	// Kafka 시작
	private static final KafkaContainer KAFKA_CONTAINER;
	private static final String KAFKA_FULL_IMAGE_NAME = "apache/kafka";

	static {
		KAFKA_CONTAINER = new KafkaContainer(DockerImageName.parse(KAFKA_FULL_IMAGE_NAME));
		KAFKA_CONTAINER.start();
		System.setProperty(
			"application.kafka-property.bootstrap-servers",
			KAFKA_CONTAINER.getBootstrapServers()
		);
	}
}
