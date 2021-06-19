package com.ecommerce.cart.persistence.repository;

import com.ecommerce.cart.aggregate.inventory.InventoryItem;

import java.util.Optional;

public interface InventoryRepository {
    Optional<InventoryItem> findById(String id);

    void saveOrUpdate(InventoryItem inventoryItem);
}
