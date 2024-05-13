package com.example.sri04.producer;

import com.example.sri04.config.JmsConfig;
import com.example.sri04.model.HelloMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class HelloWorldTopicProducer {
    private final JmsTemplate jmsTemplate;
    private final static Logger LOG = LoggerFactory.getLogger(HelloWorldTopicProducer.class);

//    @Scheduled(fixedRate = 2500)
    public void sendHello() {
        HelloMessage message = HelloMessage.builder()
                .id(HelloMessage.nextId())
                .createdAt(LocalDateTime.now())
                .message("Hello World")
                .build();

        jmsTemplate.convertAndSend(JmsConfig.TOPIC_HELLO_WORLD, message);
        LOG.info("Sent message: {}", message);
    }
}
