package com.ecommerce.cart.service.dto;

import lombok.Getter;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Getter
public class RequestDto {

    private List<RequestItemDto> items;

    @Getter
    public static class RequestItemDto {

        private String itemId;

        @Range(min = 1, max = 20, message = "Quantity must be between 1 and 20")
        private int quantity;
    }
}
