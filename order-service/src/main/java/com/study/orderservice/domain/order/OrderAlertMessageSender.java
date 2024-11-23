package com.study.orderservice.domain.order;

public interface OrderAlertMessageSender {

	void sendOrderProcessedAlert(OrderEventCommand.SendOrderProcessedAlert message);

	void sendOrderFailureAlert(OrderEventCommand.SendOrderFailureAlert command);

}
