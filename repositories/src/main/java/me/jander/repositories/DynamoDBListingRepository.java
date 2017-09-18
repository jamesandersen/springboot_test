package me.jander.repositories;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import me.jander.models.Listing;
import me.jander.models.Photo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DynamoDBListingRepository implements ListingRepository {

    private final AmazonDynamoDB client;
    private final DynamoDBMapper mapper;

    private final String listingTableName;

    public DynamoDBListingRepository(@Value("${dynamodb.listings.tablename}") String tableName) {
        this.client = AmazonDynamoDBClientBuilder.standard().build();
        this.mapper = new DynamoDBMapper(client);
        this.listingTableName = tableName;
    }

    @Override
    public Listing getById(String id) {
        String partitionKey = id;

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":v1", new AttributeValue().withS(partitionKey));

        DynamoDBQueryExpression<Listing> queryExpression = new DynamoDBQueryExpression<Listing>()
                .withKeyConditionExpression("id = :v1")
                .withExpressionAttributeValues(eav);

        List<Listing> listings = mapper.query(Listing.class, queryExpression);
        return listings.stream().findFirst().orElse(null);
    }

    public Photo addPhoto(String listingId, Photo photo)
            throws JsonProcessingException, RuntimeException {

        Map<String, AttributeValue> values = new HashMap<String, AttributeValue>(){
            {
                put(":new_photos", photoToAttribute(photo));
            }
        };

        val update = new UpdateItemRequest()
                .addKeyEntry("id", new AttributeValue(listingId))
                .withUpdateExpression("SET #photos = list_append(#photos, :new_photos)")

                .withExpressionAttributeNames(new HashMap<String, String>(){
                    {
                        put("#photos", "photos");
                    }
                })
                .withExpressionAttributeValues(values);

        val result = this.client.updateItem(update);

        if (result.getSdkHttpMetadata().getHttpStatusCode() == 200) {
            return photo;
        }

        throw new RuntimeException(String.format("Failed to add photo: %d",
                result.getSdkHttpMetadata().getHttpStatusCode()));
    }

    private static AttributeValue photoToAttribute(Photo photo)
        throws JsonProcessingException {

        val json = new ObjectMapper().writeValueAsString(photo);
        Item item = new Item().withJSON("document", json);
        Map<String,AttributeValue> attributes = InternalUtils.toAttributeValues(item);
        return attributes.get("document");
    }
}
