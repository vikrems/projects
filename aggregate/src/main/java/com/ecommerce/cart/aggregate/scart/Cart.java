package com.ecommerce.cart.aggregate.scart;

import com.ecommerce.cart.aggregate.vo.Price;
import com.ecommerce.cart.aggregate.vo.Quantity;
import lombok.Getter;

import java.util.List;

@Getter
public class Cart {

    private final String cartId;
    private final List<LineItem> items;
    private Price totalPrice;
    private Quantity totalQuantity;

    public Cart(String cartId, List<LineItem> items) {
        this.cartId = cartId;
        this.items = items;
        totalPrice = new Price();
        totalQuantity = new Quantity();

        for (LineItem eachItem : items) {
            totalPrice = totalPrice.incrementPrice(eachItem.getPrice());
            totalQuantity = totalQuantity.incrementQty(eachItem.getQuantity());
        }
    }

    public void addItems(List<LineItem> items) {

    }
}
