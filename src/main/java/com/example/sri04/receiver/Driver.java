package com.example.sri04.receiver;

import com.example.sri04.config.JmsConfig;
import com.example.sri04.model.DriverMessage;
import com.example.sri04.model.DriverResponse;
import com.example.sri04.model.WarningMessage;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class Driver {
    private final static Logger LOG = LoggerFactory.getLogger(Driver.class);
    private final JmsTemplate jmsTemplate;
    private final JmsMessagingTemplate jmsMessagingTemplate;

    @JmsListener(destination = JmsConfig.TOPIC_STATUS_MONITOR_BREAKDOWN, containerFactory = "topicConnectionFactory")
    public void receiveAwariaMessage(@Payload WarningMessage convertedMessage,
                                    @Headers MessageHeaders messageHeaders,
                                    Message message) {
        LOG.info("Received breakdown message: {}", convertedMessage.getMessage());

        DriverMessage driverMessage = DriverMessage.builder()
                .id(DriverMessage.nextId())
                .createdAt(LocalDateTime.now())
                .message("I need to make a pit stop.")
                .build();

        jmsMessagingTemplate.setJmsTemplate(jmsTemplate);
        LOG.info("I'm about to send a message: {}", driverMessage.getMessage());
        DriverResponse responseMsg = jmsMessagingTemplate.convertSendAndReceive(
                JmsConfig.QUEUE_SEND_AND_RECEIVE,
                driverMessage,
                DriverResponse.class
        );
        String responseText = responseMsg.getMessage();
        LOG.info("I've received a response (decision): {}", responseText);
    }
}
