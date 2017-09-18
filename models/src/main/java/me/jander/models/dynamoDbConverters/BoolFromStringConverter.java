package me.jander.models.dynamoDbConverters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

public class BoolFromStringConverter implements DynamoDBTypeConverter<String, Boolean> {
    @Override
    public String convert(Boolean aBoolean) {
        return aBoolean ? "1" : "0";
    }

    @Override
    public Boolean unconvert(String s) {
        return s.equals("1") ? true : false;
    }
}
