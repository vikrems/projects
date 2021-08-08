package com.ecommerce.cart.persistence.repository;

import com.ecommerce.cart.aggregate.scart.Cart;

import java.util.List;
import java.util.Optional;

public interface CartRepository {

    void saveCart(Cart cart);

    Optional<Cart> findByCartId(String cartId);

    List<Cart> findAssociatedCarts(String itemId);

}
