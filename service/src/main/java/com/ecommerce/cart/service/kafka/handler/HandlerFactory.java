package com.ecommerce.cart.service.kafka.handler;

import com.ecommerce.cart.service.kafka.message.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HandlerFactory {

    private final InsertHandler insertHandler;
    private final UpdateHandler updateHandler;
    private final DeleteHandler deleteHandler;

    public MessageHandler fetchHandler(EventType eventType) {
        switch (eventType) {
            case INSERT:
                return insertHandler;
            case UPDATE:
                return updateHandler;
            case DELETE:
                return deleteHandler;
        }
        throw new IllegalStateException("Incorrect Handler " + eventType);
    }
}
