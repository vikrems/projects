package com.ecommerce.cart.aggregate.inventory;

import lombok.*;

import java.math.BigDecimal;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItem {

    private String itemId;
    private String name;
    private BigDecimal price;
    private int quantity;
}
