package com.ecommerce.cart.service.kafka.message;

import com.ecommerce.cart.aggregate.inventory.InventoryItem;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Message {

    private Header header;
    private InventoryItem attributes;
}
