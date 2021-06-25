package com.ecommerce.cart.persistence.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.ecommerce.cart.aggregate.inventory.InventoryItem;
import com.ecommerce.cart.persistence.entity.DbEntity;
import com.ecommerce.cart.persistence.mapper.DomainToDbEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DynamoDbInventoryRepository implements InventoryRepository {

    private final DynamoDBMapper dynamoDBMapper;
    private final DomainToDbEntity domainToDbEntity;

    @Override
    public Optional<InventoryItem> findById(String id) {
        DbEntity dbEntity = dynamoDBMapper.load(DbEntity.class, id, id);
        InventoryItem inventoryItem = domainToDbEntity.dbEntityToInventoryItem(dbEntity);
        return Optional.ofNullable(inventoryItem);
    }

    @Override
    public void saveOrUpdate(InventoryItem inventoryItem) {
        DbEntity dbEntity = domainToDbEntity.inventoryToDbEntity(inventoryItem);
        dynamoDBMapper.save(dbEntity);
    }
}
