package com.ecommerce.cart.aggregate.scart;

import com.ecommerce.cart.aggregate.vo.Price;
import com.ecommerce.cart.aggregate.vo.Quantity;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
public class Cart {

    private final String cartId;
    private final Map<String, CartItem> items;
    private Price totalPrice;
    private Quantity totalQuantity;

    private Cart(String cartId, Map<String, CartItem> items) {
        this.cartId = cartId;
        this.items = items;
        computeTotals();
    }

    public static Cart createNewCart(String cartId, Map<String, CartItem> items) {
        return new Cart(cartId, items);
    }


    public void mergeItems(Map<String, CartItem> newItems) {
        for (Map.Entry<String, CartItem> eachItem : newItems.entrySet()) {
            CartItem newItem = eachItem.getValue();
            items.put(eachItem.getKey(), newItem);
        }
    }

    public void reduceQtyIfRequired(String itemId, int inventoryQty) {
        CartItem cartItem = items.get(itemId);
        int delta = cartItem.getQuantity().compareTo(inventoryQty);
        if (delta > 0) {
            Quantity updatedQuantity = new Quantity(inventoryQty);
            cartItem.setQuantity(updatedQuantity);
        }
    }

    public void changePrice(String itemId, BigDecimal inventoryPrice) {
        CartItem cartItem = items.get(itemId);
        cartItem.setPrice(inventoryPrice);
        computeTotals();
    }

    private void computeTotals() {
        totalPrice = new Price();
        totalQuantity = new Quantity();
        for (Map.Entry<String, CartItem> eachEntry : items.entrySet()) {
            CartItem eachItem = eachEntry.getValue();
            totalPrice = totalPrice.incrementPrice(eachItem.getPrice(), eachItem.getQuantity().getValue());
            totalQuantity = totalQuantity.incrementQty(eachItem.getQuantity());
        }
    }
}
