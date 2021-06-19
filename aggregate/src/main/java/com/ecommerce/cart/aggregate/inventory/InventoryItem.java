package com.ecommerce.cart.aggregate.inventory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
@RequiredArgsConstructor
public class InventoryItem {

    private final String itemId;
    private final String name;
    private final BigDecimal price;
    private final int quantity;
}
