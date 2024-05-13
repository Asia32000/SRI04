package com.example.sri04.receiver;

import com.example.sri04.config.JmsConfig;
import com.example.sri04.model.HelloMessage;
import jakarta.jms.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldTopicReceiver1 {
    private final static Logger LOG = LoggerFactory.getLogger(HelloWorldTopicReceiver1.class);

    @JmsListener(destination = JmsConfig.TOPIC_HELLO_WORLD, containerFactory = "topicConnectionFactory")
    public void receiveHelloMessage(@Payload HelloMessage convertedMessage,
                                    @Headers MessageHeaders messageHeaders,
                                    Message message) {
        LOG.info("Received a message{}", convertedMessage);
    }
}
