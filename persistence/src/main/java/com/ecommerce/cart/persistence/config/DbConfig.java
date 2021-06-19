package com.ecommerce.cart.persistence.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.SaveBehavior.UPDATE_SKIP_NULL_ATTRIBUTES;

@Configuration
public class DbConfig {

    @Bean
    public DynamoDBMapper dynamoDBMapper() {
        DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
                .withSaveBehavior(UPDATE_SKIP_NULL_ATTRIBUTES)
                .build();

        return new DynamoDBMapper(amazonDynamoDB(), mapperConfig);
    }

    private AmazonDynamoDB amazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder
                        .EndpointConfiguration("http://localhost:8000",
                        "us-east-1"))
                .build();
    }
}
