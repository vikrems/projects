package com.ecommerce.cart.service.kafka.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class Header {

    private final String eventId;
    private final EventType eventType;
    private final String eventTime;
}
