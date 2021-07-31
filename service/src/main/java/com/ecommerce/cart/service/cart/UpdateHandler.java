package com.ecommerce.cart.service.cart;

import com.ecommerce.cart.aggregate.exception.InvalidQuantityException;
import com.ecommerce.cart.aggregate.exception.PriceExceededException;
import com.ecommerce.cart.aggregate.inventory.InventoryItem;
import com.ecommerce.cart.aggregate.scart.Cart;
import com.ecommerce.cart.aggregate.scart.CartItem;
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
        for (Cart eachCart : cartList) {
            CartItem cartItem;
            try {
                cartItem = new CartItem(inventoryItem.getItemId(),
                        inventoryItem.getName(),
                        inventoryItem.getPrice(),
                        inventoryItem.getQuantity());
                eachCart.mergeItem(cartItem);
            } catch (PriceExceededException | InvalidQuantityException ex) { //In this situation - InvalidQuantityException is thrown only when the quantity is zero
                eachCart.deleteCartItem(inventoryItem.getItemId());
            }
            cartRepository.saveCart(eachCart);
        }
        log.info(message.toString());
    }
}
