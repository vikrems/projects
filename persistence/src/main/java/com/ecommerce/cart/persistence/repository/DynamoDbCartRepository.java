package com.ecommerce.cart.persistence.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.ecommerce.cart.aggregate.scart.Cart;
import com.ecommerce.cart.aggregate.scart.LineItem;
import com.ecommerce.cart.persistence.entity.DbEntity;
import com.ecommerce.cart.persistence.mapper.DomainToDbEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.ecommerce.cart.persistence.entity.DbEntity.entityWithPartitionKey;

@Repository
@RequiredArgsConstructor
public class DynamoDbCartRepository implements CartRepository {

    private final DynamoDBMapper dynamoDBMapper;
    private final DomainToDbEntity domainToDbEntity;

    @Override
    public void saveCart(Cart cart) {
        DbEntity dbEntity = domainToDbEntity.cartToDbEntity(cart);
        dynamoDBMapper.save(dbEntity);
    }

    @Override
    public void saveCartItem(Cart cart, LineItem lineItem) {
        DbEntity dbEntity = domainToDbEntity.lineItemToDbEntity(cart, lineItem);
        dynamoDBMapper.save(dbEntity);
    }

    @Override
    public Optional<Cart> findByCartId(String cartId) {
        DynamoDBQueryExpression<DbEntity> queryExpression = new DynamoDBQueryExpression<>();
        DbEntity entity = entityWithPartitionKey(cartId);
        queryExpression.setHashKeyValues(entity);
        List<DbEntity> dbEntityList = dynamoDBMapper.query(DbEntity.class, queryExpression);
        if (dbEntityList.isEmpty())
            return Optional.empty();
        return Optional.of(domainToDbEntity.dbEntityToCart(dbEntityList));
    }

    @Override
    public void delete(String cartId) {

    }
}
