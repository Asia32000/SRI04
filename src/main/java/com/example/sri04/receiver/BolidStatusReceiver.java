package com.example.sri04.receiver;

import com.example.sri04.config.JmsConfig;
import com.example.sri04.model.BolidMessage;
import jakarta.jms.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class BolidStatusReceiver {
    private final static Logger LOG = LoggerFactory.getLogger(BolidStatusReceiver.class);

    @JmsListener(destination = JmsConfig.TOPIC_BOLID, containerFactory = "topicConnectionFactory")
    public void receiveBolidMessage(@Payload BolidMessage convertedMessage,
                                    @Headers MessageHeaders messageHeaders,
                                    Message message) {
        LOG.info("\nBolid status from: {} \nID: {} \nEngine temperature: {} \nTire pressure: {} \nOil pressure: {} \nFuel level: {}",
                convertedMessage.getCreatedAt(),
                convertedMessage.getId(),
                convertedMessage.getEngineTemperature(),
                convertedMessage.getTirePressure(),
                convertedMessage.getOilPressure(),
                convertedMessage.getFuelLevel());
    }
}
