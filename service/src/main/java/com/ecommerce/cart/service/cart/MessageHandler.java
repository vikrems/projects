package com.ecommerce.cart.service.cart;

import com.ecommerce.cart.service.kafka.message.Message;

public interface MessageHandler {

    void process(Message message);
}
