package com.ecommerce.cart.service.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ResponseDto {

    private final BigDecimal totalPrice;
    private final int totalQuantity;
    private final List<ItemDto> items;

    @Getter
    @RequiredArgsConstructor
    public static class ItemDto {
        private final String itemId;
        private final String name;
        private final BigDecimal price;
        private final int quantity;
    }
}
