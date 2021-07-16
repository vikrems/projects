package com.ecommerce.cart.service.kafka.handler;

import com.ecommerce.cart.aggregate.inventory.InventoryItem;
import com.ecommerce.cart.aggregate.scart.Cart;
import com.ecommerce.cart.persistence.repository.CartRepository;
import com.ecommerce.cart.persistence.repository.InventoryRepository;
import com.ecommerce.cart.service.kafka.message.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateHandler implements MessageHandler {

    private final InventoryRepository inventoryRepository;
    private final CartRepository cartRepository;

    @Override
    public void process(Message message) {
        InventoryItem inventoryItem = message.getAttributes();
        inventoryRepository.saveOrUpdate(inventoryItem);
        List<Cart> cartList = cartRepository.findAssociatedCarts(message.getAttributes().getItemId());

        log.info(message.toString());
    }
}
