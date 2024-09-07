package com.sagitDevos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;


@Service
public class KafkaProducer {
    @Autowired
    private KafkaTemplate <String, String> kafkaTemplate;

    public void sendMessageToTopic(String message) {
        kafkaTemplate.send("topcABCD", message);
    }
}
