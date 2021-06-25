package com.ecommerce.cart.aggregate.scart;

import com.ecommerce.cart.aggregate.exception.InvariantViolationException;
import com.ecommerce.cart.aggregate.vo.Quantity;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class LineItem {

    private final String itemId;
    private final String name;
    private final BigDecimal price;
    private final Quantity quantity;

    private LineItem(String itemId, String name, BigDecimal price, Quantity quantity) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public static LineItem createLineItemFromDb(String itemId, String name,
                                                BigDecimal price, int quantity) {
        return new LineItem(itemId, name, price, new Quantity(quantity));
    }

    public static LineItem createLineItemFromApi(String itemId, String name,
                                              BigDecimal price, int desiredQty,
                                              int inventoryQty) {
        if (desiredQty > inventoryQty)
            throw new InvariantViolationException("Quantity specified for itemId "
                    + desiredQty + " exceeds the availability");
        return new LineItem(itemId, name, price, new Quantity(desiredQty));
    }
}
