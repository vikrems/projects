package com.ecommerce.cart.persistence.mapper;

import com.ecommerce.cart.aggregate.inventory.InventoryItem;
import com.ecommerce.cart.aggregate.scart.Cart;
import com.ecommerce.cart.aggregate.scart.CartItem;
import com.ecommerce.cart.persistence.entity.DbEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class Mapper {

    private static final int BOUND = 10;

//    public List<DbEntity> cartToDbEntity(Cart cart) {
//        DbEntity cartEntity = DbEntity
//                .builder()
//                .partitionKey(cart.getCartId())
//                .sortKey(cart.getCartId())
//                .gsiKey(new Random().nextInt(BOUND))
//                .lastUpdatedTime(DateTime.now())
//                .build();
//    }

    public DbEntity lineItemToDbEntity(Cart cart, CartItem cartItem) {
        return DbEntity
                .builder()
                .partitionKey(cart.getCartId())
                .sortKey(cartItem.getItemId())
                .name(cartItem.getName())
                .quantity(cartItem.getQuantity().getValue())
                .price(cartItem.getPrice())
                .build();
    }

    public Cart dbEntityToCart(List<DbEntity> dbEntityList) {
        Map<String, CartItem> lineItems = dbEntityList.stream()
                .filter(dbEntity -> !dbEntity.getPartitionKey().equals(dbEntity.getSortKey()))
                .map(lineItemMapper)
                .collect(Collectors.toMap(CartItem::getItemId, Function.identity()));
        String cartId = dbEntityList.get(0).getPartitionKey();
        return new Cart(cartId, lineItems);
    }

    public DbEntity inventoryToDbEntity(InventoryItem inventoryItem) {
        return DbEntity.builder()
                .partitionKey(inventoryItem.getItemId())
                .sortKey(inventoryItem.getItemId())
                .name(inventoryItem.getName())
                .price(inventoryItem.getPrice())
                .quantity(inventoryItem.getQuantity())
                .build();
    }

    public InventoryItem dbEntityToInventoryItem(DbEntity dbEntity) {
        return new InventoryItem(dbEntity.getPartitionKey(),
                dbEntity.getName(),
                dbEntity.getPrice(),
                dbEntity.getQuantity());
    }

    private final Function<DbEntity, CartItem> lineItemMapper = dbEntity ->
            new CartItem(dbEntity.getSortKey(),
                    dbEntity.getName(),
                    dbEntity.getPrice(),
                    dbEntity.getQuantity());
}
