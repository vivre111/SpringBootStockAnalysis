package com.austin.demo.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
class KafkaTopicConfig {

	@Value("${io.reflectoring.kafka.topic}")
	private String topic;

	@Bean
	NewTopic topicStock() {
		return TopicBuilder.name(topic).build();
	}

}
