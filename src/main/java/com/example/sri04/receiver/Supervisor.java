package com.example.sri04.receiver;

import com.example.sri04.config.JmsConfig;
import com.example.sri04.model.*;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@RequiredArgsConstructor
@Component
public class Supervisor {
    private final JmsTemplate jmsTemplate;
    private final static Logger LOG = LoggerFactory.getLogger(Supervisor.class);
    private final Random random = new Random();


    @JmsListener(destination = JmsConfig.QUEUE_SEND_AND_RECEIVE)
    public void receiveAndRespond(@Payload DriverMessage convertedMessage,
                                  @Headers MessageHeaders headers,
                                  Message message) throws JMSException {
        LOG.info("Received a message: {}", convertedMessage.getMessage());
        DriverResponse msg = DriverResponse.builder()
                .id(DriverResponse.nextId())
                .correlatedMessageId(convertedMessage.getId())
                .createdAt(LocalDateTime.now())
                .message(String.valueOf(SupervisorResponse.values()[new Random().nextInt(SupervisorResponse.values().length)]))
                .build();

        Destination replyTo = message.getJMSReplyTo();
        jmsTemplate.convertAndSend(replyTo, msg);
    }
}
