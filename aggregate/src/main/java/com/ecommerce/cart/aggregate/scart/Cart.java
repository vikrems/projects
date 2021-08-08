package com.ecommerce.cart.aggregate.scart;

import com.ecommerce.cart.aggregate.vo.Price;
import com.ecommerce.cart.aggregate.vo.Quantity;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Map;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cart {

    @EqualsAndHashCode.Include
    private final String cartId;
    private final Map<String, CartItem> items;
    private Price totalPrice;
    private Quantity totalQuantity;

    public Cart(String cartId, Map<String, CartItem> items) {
        this.cartId = cartId;
        this.items = items;
        computeTotals();
    }

    public void mergeItems(Map<String, CartItem> newItems) {
        for (Map.Entry<String, CartItem> eachItem : newItems.entrySet()) {
            CartItem newItem = eachItem.getValue();
            items.put(eachItem.getKey(), newItem);
        }
        computeTotals();
    }

    public void mergeItem(CartItem updatedItem) {
        CartItem existingItem = items.get(updatedItem.getItemId());
        if (existingItem.getQuantity().compareTo(updatedItem.getQuantity()) > 0)
            existingItem.setQuantity(updatedItem.getQuantity());
        if (existingItem.getPrice().compareTo(updatedItem.getPrice()) != 0)
            existingItem.setPrice(updatedItem.getPrice());
        computeTotals();
    }

    public void deleteCartItem(String itemId) {
        items.remove(itemId);
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
