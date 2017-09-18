package me.jander.services;

import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.val;
import me.jander.models.Listing;
import me.jander.models.Photo;
import me.jander.queues.ListingQueue;
import me.jander.queues.ListingQueueRequest;
import me.jander.queues.ListingQueueRequestType;
import me.jander.repositories.ListingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static me.jander.util.AsyncUtil.withCompletionException;

@Service
public class ListingService {

    private final ListingRepository listings;

    private final ListingQueue photoQueue;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ListingService(ListingRepository listingRepo, ListingQueue photoQueue) {

        this.listings = listingRepo;
        this.photoQueue = photoQueue;
    }

    @Async
    public CompletableFuture<Listing> getById(String id) {
        return CompletableFuture.supplyAsync(() -> this.listings.getById((id)));
    }

    public SendMessageResult requestMapPhoto(String id)
            throws JsonProcessingException {
        val request = new ListingQueueRequest(id, ListingQueueRequestType.CREATE_MAP_PHOTO);
        SendMessageResult sendMessageResult = photoQueue.enqueueListing(request);
        return sendMessageResult;
    }

    /**
     * Fetch a map image for the listing
     * @param listingId
     * @return
     */
    public CompletableFuture<Photo> getMapPhoto(String listingId) {
        val listingFuture = this.getById(listingId);
        return listingFuture.thenApply(withCompletionException(listing -> {
            val photos = Optional.ofNullable(listing.getPhotos()).orElse(Collections.<Photo>emptyList());
            val existingMap = photos.stream()
                    .filter(photo -> photo.getMap()).findFirst().orElse(null);
            if (existingMap != null) {
                logger.debug(String.format("Using existing map photo for %s", listingId));
                return existingMap;
            } else {
                val newPhoto = generateMapPhoto(listing);
                logger.debug(String.format("Generated new map photo %s for listing %s", newPhoto.getId(), listingId));
                this.listings.addPhoto(listing.getId(), newPhoto);
                return newPhoto;
            }
        }));
    }

    /**
     * Generate a map image for the listing
     * @param listing
     * @return
     */
    private Photo generateMapPhoto(Listing listing) {

        val photo = new Photo();
        photo.setId(UUID.randomUUID().toString());
        photo.setMap(true);
        return photo;
    }

}
