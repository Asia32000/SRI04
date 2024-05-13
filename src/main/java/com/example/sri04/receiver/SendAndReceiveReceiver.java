package com.example.sri04.receiver;

import com.example.sri04.config.JmsConfig;
import com.example.sri04.model.HelloMessage;
import com.example.sri04.model.HelloResponse;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import jakarta.jms.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SendAndReceiveReceiver {
    private final JmsTemplate jmsTemplate;
    private final static Logger LOG = LoggerFactory.getLogger(SendAndReceiveReceiver.class);

    @JmsListener(destination = JmsConfig.QUEUE_SEND_AND_RECEIVE)
    public void receiveAndRespond(@Payload HelloMessage convertedMessage,
                                  @Headers MessageHeaders headers,
                                  Message message) throws JMSException {
        LOG.info("Received a message: {}", convertedMessage);
        HelloResponse msg = HelloResponse.builder()
                .id(HelloResponse.nextId())
                .correlatedMessageId(convertedMessage.getId())
                .createdAt(LocalDateTime.now())
                .message("You're welcome.")
                .build();

        Destination replyTo = message.getJMSReplyTo();
        jmsTemplate.convertAndSend(replyTo, msg);
    }
}
