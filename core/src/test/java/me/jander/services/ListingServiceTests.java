package me.jander.services;

import lombok.val;
import me.jander.models.Listing;
import me.jander.models.Photo;
import me.jander.queues.ListingQueue;
import me.jander.repositories.ListingRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ListingServiceTests {


    @MockBean
    private ListingRepository listings;

    @MockBean
    private ListingQueue listingQueue;

    private ListingService service;

    private final String testListingId = "adl32s234kf1213jas";

    @Before
    public void setup() throws Exception {
        this.service = new ListingService(this.listings, this.listingQueue);
    }


    @Test
    public void testGetListing() throws Exception {
        val listing = new Listing();
        listing.setId(testListingId);
        when(listings.getById(testListingId)).thenReturn(listing);

        val result = this.service.getById(testListingId).get();

        verify(listings, atLeastOnce()).getById(testListingId);
        assertThat(result.getId()).isEqualTo(testListingId);

    }

    @Test
    public void testGetMapPhoto() throws Exception {
        val listing = new Listing();
        listing.setId(testListingId);
        when(listings.getById(testListingId)).thenReturn(listing);

        val result = this.service.getMapPhoto(testListingId).get();

        verify(listings, atLeastOnce()).getById(testListingId);
        verify(listings, atLeastOnce()).addPhoto(eq(testListingId), any());
        assertThat(result.getMap()).isTrue();

    }
}
