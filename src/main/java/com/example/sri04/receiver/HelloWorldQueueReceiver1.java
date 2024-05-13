package com.example.sri04.receiver;

import com.example.sri04.config.JmsConfig;
import com.example.sri04.model.HelloMessage;
import jakarta.jms.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldQueueReceiver1 {
    private final static Logger LOG = LoggerFactory.getLogger(HelloWorldQueueReceiver1.class);

    @JmsListener(destination = JmsConfig.QUEUE_HELLO_WORLD, containerFactory = "queueConnectionFactory")
    public void receiveHelloMessage(@Payload HelloMessage convertedMessage) {
        LOG.info("Receiver a message:"+convertedMessage);
    }
}
