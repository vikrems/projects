package com.ecommerce.cart.service.kafka;

import com.ecommerce.cart.aggregate.inventory.InventoryItem;
import com.ecommerce.cart.persistence.repository.InventoryRepository;
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

    @KafkaListener(topics = "${spring.kafka.consumer.properties.topic}",
            groupId = "${spring.kafka.consumer.properties.group}")
    public void listenGroupFoo(String msgStr, Acknowledgment acknowledgment) {
        log.info("Received Message: {}", msgStr);
        try {
            Message message = objectMapper.readValue(msgStr, Message.class);
            InventoryItem inventoryItem = message.getAttributes();
            inventoryRepository.saveOrUpdate(inventoryItem);
            log.info(message.toString());
        } catch (IOException e) {
            log.error("Error while deserializing message {}", e.getMessage());
        } finally {
            acknowledgment.acknowledge();
        }
    }
}
