package me.jander.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import me.jander.models.dynamoDbConverters.BoolFromStringConverter;
import me.jander.models.jsonSerializers.StringBooleanSerializer;

@DynamoDBDocument
public class Photo {
    @Getter(onMethod_={
            @DynamoDBAttribute(attributeName = "contentId"),
            @JsonProperty("contentId")
    })
    @Setter
    private String Id;

    @Getter(onMethod_={
            @DynamoDBAttribute(attributeName = "contentDescription"),
            @JsonProperty("contentDescription")
    })
    @Setter
    private String Description;

    @Getter(onMethod_={
            @DynamoDBAttribute(attributeName = "contentType"),
            @JsonProperty("contentType")
    })
    @Setter
    private String Type;

    @Getter(onMethod_={
            @DynamoDBAttribute(attributeName = "location"),
            @JsonProperty("location")
    })
    @Setter
    private String Url;

    @Getter(onMethod_={
            @DynamoDBAttribute(attributeName = "objectId"),
            @JsonProperty("objectId")
    })
    @Setter
    private String ObjectId;

    @Getter(onMethod_={
            @DynamoDBAttribute(attributeName = "preferred"),
            @DynamoDBTypeConverted(converter = BoolFromStringConverter.class),
            @JsonProperty("preferred")
    })
    @Setter
    private Boolean Preferred;

    @Getter(onMethod_={
            @DynamoDBAttribute(attributeName = "map"),
            @DynamoDBTypeConverted(converter = BoolFromStringConverter.class),
            @JsonProperty("map"),
            @JsonSerialize(using = StringBooleanSerializer.class)
    })
    @Setter
    private Boolean Map;
}
