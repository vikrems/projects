package com.ecommerce.cart.persistence.mapper;

import com.ecommerce.cart.aggregate.inventory.InventoryItem;
import com.ecommerce.cart.aggregate.scart.Cart;
import com.ecommerce.cart.aggregate.scart.LineItem;
import com.ecommerce.cart.persistence.entity.DbEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DomainToDbEntity {

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
        Map<String, LineItem> lineItems = dbEntityList.stream()
                .filter(dbEntity -> !dbEntity.getPartitionKey().equals(dbEntity.getSortKey()))
                .map(lineItemMapper)
                .collect(Collectors.toMap(LineItem::getItemId, Function.identity()));
        String cartId = dbEntityList.get(0).getPartitionKey();
        return Cart.createNewCart(cartId, lineItems);
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

    private final Function<DbEntity, LineItem> lineItemMapper = dbEntity ->
            LineItem.createLineItemFromDb(dbEntity.getSortKey(),
                    dbEntity.getName(),
                    dbEntity.getPrice(),
                    dbEntity.getQuantity());
}
