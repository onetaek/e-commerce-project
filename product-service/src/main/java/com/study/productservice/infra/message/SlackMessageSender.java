package com.study.productservice.infra.message;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.slack.api.Slack;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;
import com.slack.api.webhook.Payload;
import com.study.productservice.common.exception.ErrorMessageSender;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SlackMessageSender implements ErrorMessageSender {

	@Value("${slack.webhook-uri}")
	private String webhookUri;
	@Value("${slack.channel}")
	private String channel;

	@Override
	public void sendErrorMessage(String message) {
		try {
			// Slack 블록 메시지
			var payload = Payload.builder()
				.channel(channel)
				.username("서버 에러발행!")
				.iconEmoji(":warning:")
				.blocks(List.of(
					SectionBlock.builder()
						.text(MarkdownTextObject.builder()
							.text(message)
							.build())
						.build()
				))
				.build();

			Slack.getInstance().send(webhookUri, payload);
		} catch (IOException e) {
			log.error("Slack send error", e);
		}
	}
}
