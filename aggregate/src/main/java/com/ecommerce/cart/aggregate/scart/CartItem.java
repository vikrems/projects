package com.ecommerce.cart.aggregate.scart;

import com.ecommerce.cart.aggregate.exception.PriceExceededException;
import com.ecommerce.cart.aggregate.vo.Quantity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
public class CartItem {

    private final String itemId;
    private final String name;

    @Setter
    private BigDecimal price;

    @Setter
    private Quantity quantity;

    private CartItem(String itemId, String name, BigDecimal price, Quantity quantity) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public static CartItem createLineItemFromDb(String itemId, String name,
                                                BigDecimal price, int quantity) {
        return new CartItem(itemId, name, price, new Quantity(quantity));
    }

    public static CartItem createLineItemFromApi(String itemId, String name,
                                                 BigDecimal price, int desiredQty,
                                                 int inventoryQty) {
        if (desiredQty > inventoryQty)
            throw new PriceExceededException("Quantity specified for itemId "
                    + desiredQty + " exceeds the availability");
        return new CartItem(itemId, name, price, new Quantity(desiredQty));
    }
}
