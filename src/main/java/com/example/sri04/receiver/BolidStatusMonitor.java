package com.example.sri04.receiver;

import com.example.sri04.config.JmsConfig;
import com.example.sri04.model.BolidMessage;
import com.example.sri04.model.FuelLevel;
import com.example.sri04.model.WarningMessage;
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
import org.springframework.jms.core.JmsTemplate;

@RequiredArgsConstructor
@Component
public class BolidStatusMonitor {
    private final static Logger LOG = LoggerFactory.getLogger(BolidStatusMonitor.class);
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.TOPIC_BOLID, containerFactory = "topicConnectionFactory")
    public void receiveBolidMessage(@Payload BolidMessage convertedMessage,
                                    @Headers MessageHeaders messageHeaders,
                                    Message message) {
        int engTemperature = convertedMessage.getEngineTemperature();
        double tirePressure = convertedMessage.getTirePressure();
        double oilPressure = convertedMessage.getOilPressure();
        FuelLevel fuelLevel = convertedMessage.getFuelLevel();

        boolean isTemperatureIncorrect = engTemperature > 120 || engTemperature < 100;
        boolean isTirePressureIncorrect = tirePressure > 1.8 || tirePressure < 1.3;
        boolean isOilPressureIncorrect = oilPressure > 5 || oilPressure < 3;

        boolean isTemperatureDangerous = engTemperature > 123;
        boolean isTirePressureDangerous = tirePressure > 2.0;
        boolean isOilPressureDangerous = oilPressure > 5.2;

        if (isTemperatureDangerous ||
                isTirePressureDangerous ||
                isOilPressureDangerous ||
                fuelLevel == FuelLevel.VERY_LOW
        ) {
            WarningMessage warningMessage = WarningMessage.builder()
                    .message("There was a car breakdown")
                    .build();
            jmsTemplate.convertAndSend(JmsConfig.TOPIC_STATUS_MONITOR_BREAKDOWN, warningMessage);
        } else if (isTemperatureIncorrect || isTirePressureIncorrect || isOilPressureIncorrect) {
            WarningMessage warningMessage = WarningMessage.builder()
                    .message("There was a car failure")
                    .build();
            jmsTemplate.convertAndSend(JmsConfig.TOPIC_STATUS_MONITOR_FAULT, warningMessage);
        }
    }
}
