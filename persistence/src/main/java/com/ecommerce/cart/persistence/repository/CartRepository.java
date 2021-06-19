package com.ecommerce.cart.persistence.repository;

import com.ecommerce.cart.aggregate.scart.Cart;
import com.ecommerce.cart.aggregate.scart.LineItem;

import java.util.Optional;

public interface CartRepository {

    void saveCart(Cart cart);

    void saveCartItem(Cart cart, LineItem lineItem);

    Optional<Cart> findByCartId(String cartId);

    void delete(String cartId);
}
