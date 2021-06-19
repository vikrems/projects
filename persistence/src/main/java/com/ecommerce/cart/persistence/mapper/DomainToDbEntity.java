package com.ecommerce.cart.persistence.mapper;

import com.ecommerce.cart.aggregate.inventory.InventoryItem;
import com.ecommerce.cart.aggregate.scart.Cart;
import com.ecommerce.cart.aggregate.scart.LineItem;
import com.ecommerce.cart.aggregate.vo.Quantity;
import com.ecommerce.cart.persistence.entity.DbEntity;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Component
public class DomainToDbEntity {

    private static final int BOUND = 10;

    public DbEntity cartToDbEntity(Cart cart) {
        return DbEntity
                .builder()
                .partitionKey(cart.getCartId())
                .sortKey(cart.getCartId())
                .gsiKey(new Random().nextInt(BOUND))
                .createdTime(DateTime.now())
                .build();
    }

    public DbEntity lineItemToDbEntity(Cart cart, LineItem lineItem) {
        return DbEntity
                .builder()
                .partitionKey(cart.getCartId())
                .sortKey(lineItem.getItemId())
                .name(lineItem.getName())
                .quantity(lineItem.getQuantity().getValue())
                .price(lineItem.getPrice())
                .build();
    }

    public Cart dbEntityToCart(List<DbEntity> dbEntityList) {
        List<LineItem> lineItems = dbEntityList.stream()
                .filter(dbEntity -> !dbEntity.getPartitionKey().equals(dbEntity.getSortKey()))
                .map(lineItemMapper)
                .collect(toList());
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


    private final Function<DbEntity, LineItem> lineItemMapper = dbEntity ->
            new LineItem(dbEntity.getSortKey(),
                    dbEntity.getName(),
                    dbEntity.getPrice(),
                    new Quantity(dbEntity.getQuantity()));
}
