package com.ecommerce.cart.aggregate.scart;

import com.ecommerce.cart.aggregate.vo.Quantity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CartItem {

    @EqualsAndHashCode.Include
    private final String itemId;
    private final String name;

    @Setter
    private BigDecimal price;

    @Setter
    private Quantity quantity;

    public CartItem(String itemId, String name, BigDecimal price, int quantity) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.quantity = new Quantity(quantity);
    }
}
