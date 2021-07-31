package com.ecommerce.cart.service.cart;

import com.ecommerce.cart.aggregate.inventory.InventoryItem;
import com.ecommerce.cart.persistence.repository.InventoryRepository;
import com.ecommerce.cart.service.kafka.message.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InsertHandler implements MessageHandler {

    private final InventoryRepository inventoryRepository;

    @Override
    public void process(Message message) {
        InventoryItem inventoryItem = message.getAttributes();
        inventoryRepository.saveOrUpdate(inventoryItem);
        log.info(message.toString());
    }
}
