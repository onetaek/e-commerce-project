package com.study.ecommerce.infra.order;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.study.ecommerce.presentation.order.OrderDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderSlackSender {

	@Value("${slack.webhook-uri}")
	private String webhookUri;

	@Value("${slack.channel}")
	private String channel;

	public void sendEvent(OrderDto.Event event) {
		try {
			var payload = Payload.builder()
				.channel(channel)
				.username("OrderService")
				.iconEmoji("package")
				.text(event.toString())
				.build();

			Slack.getInstance().send(webhookUri, payload);
		} catch (Exception e) {
			log.error("slack send error", e);
		}
	}
}
