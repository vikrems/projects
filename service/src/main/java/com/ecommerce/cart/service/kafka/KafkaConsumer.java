package com.ecommerce.cart.service.kafka;

import com.ecommerce.cart.persistence.repository.InventoryRepository;
import com.ecommerce.cart.service.kafka.handler.HandlerFactory;
import com.ecommerce.cart.service.kafka.handler.MessageHandler;
import com.ecommerce.cart.service.kafka.message.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final InventoryRepository inventoryRepository;
    private final ObjectMapper objectMapper;
    private final HandlerFactory handlerFactory;

    @KafkaListener(topics = "${spring.kafka.consumer.properties.topic}",
            groupId = "${spring.kafka.consumer.properties.group}")
    public void listenGroupFoo(String msgStr, Acknowledgment acknowledgment) {
        log.info("Received Message: {}", msgStr);
        try {
            Message message = objectMapper.readValue(msgStr, Message.class);
            MessageHandler messageHandler = handlerFactory.fetchHandler(message.getHeader().getEventType());
            messageHandler.process(message);
        } catch (IOException e) {
            log.error("Error while deserializing message {}", e.getMessage());
        } finally {
            acknowledgment.acknowledge();
        }
    }
}
