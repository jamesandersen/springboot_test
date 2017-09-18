package me.jander.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.jander.models.Listing;
import me.jander.models.Photo;

public interface ListingRepository {
    Listing getById(String id);
    Photo addPhoto(String listingId, Photo photo) throws JsonProcessingException;
}
