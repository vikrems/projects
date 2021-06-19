package com.ecommerce.cart.service.cart;

import com.ecommerce.cart.aggregate.inventory.InventoryItem;
import com.ecommerce.cart.aggregate.scart.Cart;
import com.ecommerce.cart.aggregate.scart.LineItem;
import com.ecommerce.cart.aggregate.vo.Quantity;
import com.ecommerce.cart.persistence.repository.CartRepository;
import com.ecommerce.cart.persistence.repository.InventoryRepository;
import com.ecommerce.cart.service.dto.RequestDto;
import com.ecommerce.cart.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final InventoryRepository inventoryRepository;
    private final CartRepository cartRepository;

    public boolean upsertCart(String cartId, RequestDto requestDto) {
        List<LineItem> lineItems = validateInventoryAvailability(requestDto);
        Optional<Cart> cartOptional = cartRepository.findByCartId(cartId);
        Cart cart;
        if (cartOptional.isEmpty()) {
            cart = new Cart(cartId, lineItems);
            cartRepository.saveCart(cart);
            return true;
        }
        cart = cartOptional.get();
        cart.addItems(lineItems);
        cartRepository.saveCart(cart);
        return false;
    }

    private List<LineItem> validateInventoryAvailability(RequestDto requestDto) {
        List<LineItem> lineItems = new LinkedList<>();
        for (RequestDto.RequestItemDto eachReqItem : requestDto.getItems()) {
            String itemId = eachReqItem.getItemId();
            InventoryItem inventoryItem = inventoryRepository.findById(itemId)
                    .orElseThrow(() -> new ResourceNotFoundException("Invalid product Id " + itemId));
            if (inventoryItem.getQuantity() < eachReqItem.getQuantity())
                throw new RuntimeException("Requested quantity " + eachReqItem.getQuantity() +
                        " is not available for the product with id:" + eachReqItem.getItemId());
            lineItems.add(new LineItem(inventoryItem.getItemId(),
                    inventoryItem.getName(),
                    inventoryItem.getPrice(),
                    new Quantity(inventoryItem.getQuantity())));
        }

        return lineItems;
    }
}
