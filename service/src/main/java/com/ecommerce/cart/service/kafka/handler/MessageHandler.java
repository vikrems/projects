package com.ecommerce.cart.service.kafka.handler;

import com.ecommerce.cart.service.kafka.message.Message;

public interface MessageHandler {

    void process(Message message);
}
