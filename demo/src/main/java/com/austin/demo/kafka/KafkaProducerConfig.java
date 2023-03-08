package com.austin.demo.kafka;

import com.austin.demo.Stock;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
class KafkaProducerConfig {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    @Value("${io.reflectoring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Bean
    public ProducerFactory<String, Stock> stockProducerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(configProps);
	}
    @Bean
    public KafkaTemplate<String, Stock> stockKafkaTemplate() {
        KafkaTemplate<String, Stock> stockKafkaTemplate =new KafkaTemplate<>(stockProducerFactory());
        stockKafkaTemplate.setProducerListener(new ProducerListener<String, Stock>() {
            @Override
            public void onSuccess(ProducerRecord<String, Stock> producerRecord, RecordMetadata recordMetadata) {
                LOG.info("my ACK from ProducerListener message: {} offset:  {}", producerRecord.value(),
                        recordMetadata.offset());
            }
        });
        return stockKafkaTemplate;
    }

    @Bean
    Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    KafkaTemplate<String, String> kafkaTemplate() {
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        kafkaTemplate.setMessageConverter(new StringJsonMessageConverter());
        kafkaTemplate.setDefaultTopic("reflectoring-user");
        kafkaTemplate.setProducerListener(new ProducerListener<String, String>() {
            @Override
            public void onSuccess(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata) {
                LOG.info("my ACK from ProducerListener message: {} offset:  {}", producerRecord.value(),
                        recordMetadata.offset());
            }
        });
        return kafkaTemplate;
    }


}
