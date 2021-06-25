package com.ecommerce.cart.persistence.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamoDBTable(tableName = "items")
public class DbEntity {

    @DynamoDBHashKey
    private String partitionKey;

    @DynamoDBRangeKey
    private String sortKey;

    @DynamoDBAttribute
    private String name;

    @DynamoDBAttribute
    private BigDecimal price;

    @DynamoDBAttribute
    private int quantity;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "GSIndex")
    private int gsiKey;

    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "GSIndex")
    @DynamoDBTypeConvertedEpochDate
    private DateTime lastUpdatedTime;

    private DbEntity(String partitionKey) {
        this.partitionKey = partitionKey;
    }

    public static DbEntity entityWithPartitionKey(String partitionKey) {
        return new DbEntity(partitionKey);
    }
}
