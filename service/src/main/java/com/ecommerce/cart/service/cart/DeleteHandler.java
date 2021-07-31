package com.ecommerce.cart.service.cart;

import com.ecommerce.cart.service.kafka.message.Message;
import org.springframework.stereotype.Component;

@Component
public class DeleteHandler implements MessageHandler {

    @Override
    public void process(Message message) {

    }
}
