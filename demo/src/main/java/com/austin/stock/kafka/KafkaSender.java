package com.austin.stock.kafka;

import com.austin.stock.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaSender {
    @Autowired
    KafkaTemplate<String, Stock> stockKafkaTemplate;
    @Autowired
    KafkaTemplate<String, String>kafkaTemplate;
    private final Logger LOG = LoggerFactory.getLogger(KafkaSender.class);

    void send(String message, String topicName) {
        LOG.info("Sending : {}", message);
        LOG.info("--------------------------------");
        kafkaTemplate.send(topicName,message);
    }

    public void sendStock(Stock s,String topicName){
        LOG.info("Sending Json Serializer : {}", s);
        LOG.info("--------------------------------");
        stockKafkaTemplate.send(topicName, s);

    }
}
