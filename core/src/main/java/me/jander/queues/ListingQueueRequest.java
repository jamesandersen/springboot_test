package me.jander.queues;

import lombok.Getter;

public class ListingQueueRequest {
    @Getter
    private String listingId;

    @Getter
    private ListingQueueRequestType type;

    public ListingQueueRequest(String id, ListingQueueRequestType type) {
        this.listingId = id;
        this.type = type;
    }
}
