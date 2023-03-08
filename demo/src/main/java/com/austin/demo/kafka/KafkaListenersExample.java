package com.austin.demo.kafka;

import com.austin.demo.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
class KafkaListenersExample {

	private final Logger LOG = LoggerFactory.getLogger(KafkaListenersExample.class);

	@KafkaListener(topics = "reflectoring-1")
	void listener(String message) {
		LOG.info("Listener [{}]", message);
	}

	/*
	@KafkaListener(topics = { "reflectoring-1", "reflectoring-2" }, groupId = "reflectoring-group-2")
	void commonListenerForMultipleTopics(String message) {
		LOG.info("MultipleTopicListener - [{}]", message);
	}

	@KafkaListener(topics = "reflectoring-bytes")
	void listenerForRoutingTemplate(String message) {
		LOG.info("RoutingTemplate BytesListener [{}]", message);
	}

	 */

	@KafkaListener(topics = "reflectoring-others")
	@SendTo("reflectoring-2")
	String listenAndReply(String message) {
		LOG.info("ListenAndReply [{}]", message);
		return "This is a reply sent to 'reflectoring-2' topic after receiving message at 'reflectoring-others' topic";
	}

	@KafkaListener(id = "1", topics = "reflectoring-user", groupId = "reflectoring-user-mc", containerFactory = "kafkaJsonListenerContainerFactory")
	void listenerWithMessageConverter(Stock stock) {
		LOG.info("MessageConverterUserListener [{}]", stock);
	}
}

