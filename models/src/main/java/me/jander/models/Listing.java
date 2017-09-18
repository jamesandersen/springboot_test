package me.jander.models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@DynamoDBTable(tableName="concierge-bot-fb-listings-prop-a-dev")
public class Listing {

    @Getter(onMethod_={@DynamoDBHashKey(attributeName="id")})
    @Setter
    private String Id;

    @Getter(onMethod_={@DynamoDBAttribute(attributeName = "listprice")})
    @Setter
    private int listPrice;

    @Getter(onMethod_={
            @DynamoDBAttribute(attributeName = "photos"),
            @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.L)})
    @Setter
    private List<Photo> Photos;

    @Getter
    @Setter
    private String type;
}
