package com.study.orderservice.infra.message;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.slack.api.Slack;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;
import com.slack.api.webhook.Payload;
import com.study.orderservice.common.exception.ErrorMessageSender;
import com.study.orderservice.domain.order.OrderAlertMessageSender;
import com.study.orderservice.domain.order.OrderEventCommand;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SlackMessageSender implements OrderAlertMessageSender, ErrorMessageSender {

	@Value("${slack.webhook-uri}")
	private String webhookUri;
	@Value("${slack.channel}")
	private String channel;

	@Override
	public void sendOrderProcessedAlert(OrderEventCommand.SendOrderProcessedAlert message) {
		try {

			// 주문 정보 구성
			StringBuilder orderItemsText = new StringBuilder();
			for (var item : message.orderItems()) {
				orderItemsText.append(String.format(
					"- 상품명: %s, 가격: %d, 수량: %d개%n",
					item.productName(),
					item.price(),
					item.amount()
				));
			}

			// 메시지 텍스트
			String formattedText = String.format(
				"*%s 님 주문이 정상적으로 처리되었습니다!*%n%n" +
					"*주문번호*: %d%n" +
					"*총 가격*: %d원%n%n" +
					"*주문 상품 정보*%n%s",
				message.userId(),
				message.orderId(),
				message.sumPrice(),
				orderItemsText
			);

			// Slack 블록 메시지
			var payload = Payload.builder()
				.channel(channel)
				.username("주문 성공")
				.iconEmoji(":package:")
				.blocks(List.of(
					SectionBlock.builder()
						.text(MarkdownTextObject.builder()
							.text(formattedText)
							.build())
						.build()
				))
				.build();

			Slack.getInstance().send(webhookUri, payload);

		} catch (Exception e) {
			log.error("Slack send error", e);
		}
	}

	@Override
	public void sendOrderFailureAlert(OrderEventCommand.SendOrderFailureAlert command) {
		try {
			// 메시지 텍스트
			String formattedText = String.format(
				"*%s 님 주문처리에 실패하였습니다*%n%n" +
					"오류 메시지: %s",
				command.userId(),
				command.errorMessage()
			);

			// Slack 블록 메시지
			var payload = Payload.builder()
				.channel(channel)
				.username("주문 실패")
				.iconEmoji(":x:") // 실패를 나타내는 이모지
				.blocks(List.of(
					SectionBlock.builder()
						.text(MarkdownTextObject.builder()
							.text(formattedText)
							.build())
						.build()
				))
				.build();

			// Slack 메시지 전송
			Slack.getInstance().send(webhookUri, payload);

		} catch (Exception e) {
			log.error("Slack send error", e);
		}
	}

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
