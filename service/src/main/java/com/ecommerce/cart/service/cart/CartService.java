package com.ecommerce.cart.service.cart;

import com.ecommerce.cart.aggregate.exception.InvalidQuantityException;
import com.ecommerce.cart.aggregate.inventory.InventoryItem;
import com.ecommerce.cart.aggregate.scart.Cart;
import com.ecommerce.cart.aggregate.scart.CartItem;
import com.ecommerce.cart.persistence.repository.CartRepository;
import com.ecommerce.cart.persistence.repository.InventoryRepository;
import com.ecommerce.cart.service.dto.RequestDto;
import com.ecommerce.cart.service.dto.ResponseDto;
import com.ecommerce.cart.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final InventoryRepository inventoryRepository;
    private final CartRepository cartRepository;

    public void upsertCart(String cartId, RequestDto requestDto) {
        Map<String, CartItem> lineItems = validateInventoryAvailability(requestDto);
        Cart cart = new Cart(cartId, lineItems);
        cartRepository.saveCart(cart);
    }

    public void addToCart(String cartId, RequestDto requestDto) {
        Cart cart = cartRepository.findByCartId(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart with ID " + cartId + " is not available"));
        Map<String, CartItem> lineItems = validateInventoryAvailability(requestDto);
        cart.mergeItems(lineItems);
        cartRepository.saveCart(cart);
    }

    public ResponseDto retrieveCart(String cartId) {
        Cart cart = cartRepository.findByCartId(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart with ID " + cartId + " is not available"));
        return createResponseDto(cart);
    }

    private Map<String, CartItem> validateInventoryAvailability(RequestDto requestDto) {
        Map<String, CartItem> cartItemMap = new HashMap<>();
        for (RequestDto.RequestItemDto eachReqItem : requestDto.getItems()) {
            String itemId = eachReqItem.getItemId();
            InventoryItem inventoryItem = inventoryRepository.findById(itemId)
                    .orElseThrow(() -> new ResourceNotFoundException("Invalid product Id " + itemId));
            if (eachReqItem.getQuantity() > inventoryItem.getQuantity())
                throw new InvalidQuantityException("Quantity specified for itemId " + itemId
                        + " exceeds the availability which is " + inventoryItem.getQuantity());
            CartItem cartItem = new CartItem(itemId, inventoryItem.getName(), inventoryItem.getPrice(),
                    eachReqItem.getQuantity());
            cartItemMap.put(itemId, cartItem);
        }
        return cartItemMap;
    }

    private ResponseDto createResponseDto(Cart cart) {
        List<ResponseDto.ItemDto> itemDtos = cart.getItems()
                .values()
                .stream()
                .map(lItem -> new ResponseDto.ItemDto(lItem.getItemId(),
                        lItem.getName(),
                        lItem.getPrice(),
                        lItem.getQuantity().getValue()))
                .collect(Collectors.toList());

        return new ResponseDto(cart.getTotalPrice().getValue(),
                cart.getTotalQuantity().getValue(),
                itemDtos);
    }
}
