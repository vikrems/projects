package com.ecommerce.cart.aggregate.scart;

import com.ecommerce.cart.aggregate.vo.Price;
import com.ecommerce.cart.aggregate.vo.Quantity;
import lombok.Getter;

import java.util.Map;

@Getter
public class Cart {

    private final String cartId;
    private Map<String, LineItem> items;
    private Price totalPrice;
    private Quantity totalQuantity;

    private Cart(String cartId, Map<String, LineItem> items) {
        this.cartId = cartId;
        this.items = items;
        totalPrice = new Price();
        totalQuantity = new Quantity();

        for (Map.Entry<String, LineItem> eachEntry : items.entrySet()) {
            LineItem eachItem = eachEntry.getValue();
            totalPrice = totalPrice.incrementPrice(eachItem.getPrice(), eachItem.getQuantity().getValue());
            totalQuantity = totalQuantity.incrementQty(eachItem.getQuantity());
        }
    }

    public static Cart createNewCart(String cartId, Map<String, LineItem> items) {
        return new Cart(cartId, items);
    }

    public void replaceItems(Map<String, LineItem> newItems) {
        this.items = newItems;
        totalPrice = new Price();
        totalQuantity = new Quantity();

        for (Map.Entry<String, LineItem> eachEntry : items.entrySet()) {
            LineItem eachItem = eachEntry.getValue();
            totalPrice = totalPrice.incrementPrice(eachItem.getPrice(), 1);
            totalQuantity = totalQuantity.incrementQty(eachItem.getQuantity());
        }
    }

    public void mergeItems(Map<String, LineItem> newItems) {
        for (Map.Entry<String, LineItem> eachItem : newItems.entrySet()) {
            if (items.containsKey(eachItem.getKey())) {
                LineItem existingItem = items.get(eachItem.getKey());
                LineItem newItem = eachItem.getValue();
                items.put(eachItem.getKey(), newItem);
                int diff = Quantity.findDelta(existingItem.getQuantity(), newItem.getQuantity());
                if (diff < 0) {
                    totalPrice = totalPrice.decrementPrice(newItem.getPrice(), Math.abs(diff));
                    totalQuantity = totalQuantity.decrementQty(newItem.getQuantity(), Math.abs(diff));
                } else {
                    totalPrice = totalPrice.incrementPrice(newItem.getPrice(), diff);
                    totalQuantity = totalQuantity.incrementQty(newItem.getQuantity());
                }
            } else {
                LineItem newItem = eachItem.getValue();
                items.put(eachItem.getKey(), newItem);
                totalPrice = totalPrice.incrementPrice(newItem.getPrice(), 1);
                totalQuantity = totalQuantity.incrementQty(newItem.getQuantity());
            }
        }
    }
}
