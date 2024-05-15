package com.example.sri04.receiver;

import com.example.sri04.config.JmsConfig;
import com.example.sri04.model.WarningMessage;
import jakarta.jms.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class Mechanic {
    private final static Logger LOG = LoggerFactory.getLogger(Mechanic.class);

    @JmsListener(destination = JmsConfig.TOPIC_STATUS_MONITOR_BREAKDOWN, containerFactory = "topicConnectionFactory")
    public void receiveAwariaMessage(@Payload WarningMessage convertedMessage,
                                     @Headers MessageHeaders messageHeaders,
                                     Message message) {
        LOG.info("Received breakdown message: {}", convertedMessage.getMessage());
    }

    @JmsListener(destination = JmsConfig.TOPIC_STATUS_MONITOR_FAULT, containerFactory = "topicConnectionFactory")
    public void receiveFaultMessage(@Payload WarningMessage convertedMessage,
                                     @Headers MessageHeaders messageHeaders,
                                     Message message) {
        LOG.info("Received fault message: {}", convertedMessage.getMessage());
    }
}
