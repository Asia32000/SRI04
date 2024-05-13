package com.example.sri04.producer;

import com.example.sri04.config.JmsConfig;
import com.example.sri04.model.BolidMessage;
import com.example.sri04.model.FuelLevel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class BolidMessageProducer {
    private final JmsTemplate jmsTemplate;
    private final static Logger LOG = LoggerFactory.getLogger(BolidMessageProducer.class);

    @Scheduled(fixedRate = 10000)
    public void sendBolidMessage() {
        BolidMessage message = BolidMessage.builder()
                .id(BolidMessage.nextId())
                .createdAt(LocalDateTime.now())
                .engineTemperature(ThreadLocalRandom.current().nextInt(100, 125))
                .tirePressure(ThreadLocalRandom.current().nextDouble(1.2, 2.1))
                .oilPressure(ThreadLocalRandom.current().nextDouble(2.9, 5.3))
                .fuelLevel(FuelLevel.values()[new Random().nextInt(FuelLevel.values().length)])
                .build();

        jmsTemplate.convertAndSend(JmsConfig.TOPIC_BOLID, message);
    }
}
