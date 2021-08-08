package com.ecommerce.cart.persistence.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.ecommerce.cart.aggregate.scart.Cart;
import com.ecommerce.cart.aggregate.scart.CartItem;
import com.ecommerce.cart.persistence.entity.DbEntity;
import com.ecommerce.cart.persistence.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.ecommerce.cart.persistence.entity.DbEntity.entityWithPartitionKey;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Repository
@RequiredArgsConstructor
public class DynamoDbCartRepository implements CartRepository {

    private final DynamoDBMapper dynamoDBMapper;
    private final Mapper mapper;

    @Override
    public void saveCart(Cart cart) {
        Map<String, DbEntity> existingEntities = existingCartEntities(cart.getCartId());
        if (existingEntities != null) {
            for (Map.Entry<String, CartItem> eachEntry : cart.getItems().entrySet()) {
                CartItem newItem = eachEntry.getValue();
                String key = primaryKey(cart.getCartId(), eachEntry.getKey());
                DbEntity existingEntity = existingEntities.get(key);
                if (notIdentical(newItem, existingEntity)) {
                    DbEntity newEntityToInsert = mapper.cartItemToDbEntity(cart, newItem);
                    dynamoDBMapper.save(newEntityToInsert);
                }
                if (nonNull(existingEntity))
                    existingEntities.remove(primaryKey(existingEntity.getPartitionKey(), existingEntity.getSortKey()));
            }
            for (Map.Entry<String, DbEntity> eachEntity : existingEntities.entrySet()) {
                dynamoDBMapper.delete(eachEntity.getValue());
            }
        } else {
            for (Map.Entry<String, CartItem> eachEntry : cart.getItems().entrySet()) {
                CartItem newItem = eachEntry.getValue();
                DbEntity newEntityToInsert = mapper.cartItemToDbEntity(cart, newItem);
                dynamoDBMapper.save(newEntityToInsert);
            }
        }
    }


    @Override
    public Optional<Cart> findByCartId(String cartId) {
        DynamoDBQueryExpression<DbEntity> queryExpression = new DynamoDBQueryExpression<>();
        DbEntity entity = entityWithPartitionKey(cartId);
        queryExpression.setHashKeyValues(entity);
        List<DbEntity> dbEntityList = dynamoDBMapper.query(DbEntity.class, queryExpression);
        if (dbEntityList.isEmpty())
            return Optional.empty();
        return Optional.of(mapper.dbEntityToCart(dbEntityList));
    }

    @Override
    public List<Cart> findAssociatedCarts(String itemId) {
        Map<String, AttributeValue> eav = Map.
                of(":v1", new AttributeValue().withS(itemId));

        DynamoDBQueryExpression<DbEntity> queryExpression = new DynamoDBQueryExpression<>();
        queryExpression.setIndexName("sortKey-index");
        queryExpression.setConsistentRead(false);
        queryExpression.setKeyConditionExpression("sortKey = :v1");
        queryExpression.setExpressionAttributeValues(eav);

        return dynamoDBMapper.query(DbEntity.class, queryExpression)
                .stream()
                .filter(entity -> !entity.getPartitionKey().equals(entity.getSortKey()))
                .map(dbEntity -> findByCartId(dbEntity.getPartitionKey()).get())
                .collect(toList());
    }

    private Map<String, DbEntity> existingCartEntities(String cartId) {
        DynamoDBQueryExpression<DbEntity> queryExpression = new DynamoDBQueryExpression<>();
        DbEntity entity = entityWithPartitionKey(cartId);
        queryExpression.setHashKeyValues(entity);
        return dynamoDBMapper.query(DbEntity.class, queryExpression)
                .stream()
                .filter(s -> !entity.getPartitionKey().equals(entity.getSortKey()))
                .collect(toMap(s -> primaryKey(s.getPartitionKey(), s.getSortKey()), Function.identity()));
    }

    private boolean notIdentical(CartItem newItem, DbEntity existingItem) {
        return isNull(existingItem) ||
                existingItem.getPrice().compareTo(newItem.getPrice()) != 0
                || newItem.getQuantity().compareTo(existingItem.getQuantity()) != 0;
    }

    private String primaryKey(String partitionKey, String sortKey) {
        return partitionKey + "_" + sortKey;
    }
}
