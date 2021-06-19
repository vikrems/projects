package com.ecommerce.cart.aggregate.scart;

import com.ecommerce.cart.aggregate.vo.Quantity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class LineItem {

    private final String itemId;
    private final String name;
    private final BigDecimal price;
    private final Quantity quantity;
}
