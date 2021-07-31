package com.ecommerce.cart.persistence.repository;

import com.ecommerce.cart.aggregate.scart.Cart;
import com.ecommerce.cart.aggregate.scart.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartRepository {

    void saveCart(Cart cart);

    void saveCartItem(Cart cart, CartItem cartItem);

    void deleteCartItem(Cart cart, CartItem cartItem);

    Optional<Cart> findByCartId(String cartId);

    List<Cart> findAssociatedCarts(String itemId);

    void delete(String cartId);
}
