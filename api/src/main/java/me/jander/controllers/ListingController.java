package me.jander.controllers;

import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.val;
import me.jander.models.Greeting;
import me.jander.models.Listing;
import me.jander.models.Photo;
import me.jander.services.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ListingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    ListingService listingService;

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

    @RequestMapping("/listing")
    public Listing listingById(@RequestParam(value="id", defaultValue="20060909014136093117000000") String id)
    throws InterruptedException, ExecutionException {
        val result = this.listingService.getById(id);
        return result.get();
    }

    @RequestMapping("/listing/queueMapPhoto")
    public SendMessageResult queueMapPhoto(@RequestParam(value="id", defaultValue="20060909014136093117000000") String id)
            throws JsonProcessingException {
        val result = this.listingService.requestMapPhoto(id);
        return result;
    }

    @RequestMapping("/listing/getMapPhoto")
    public Photo getMapPhoto(@RequestParam(value="id", defaultValue="20060909014136093117000000") String id)
            throws JsonProcessingException, ExecutionException, InterruptedException {
        val result = this.listingService.getMapPhoto(id).get();
        return result;
    }

    @RequestMapping(value="/post/greeting", method=RequestMethod.POST)
    public Greeting postGreeting(@RequestBody Greeting newGreeting) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, newGreeting.getContent()));
    }
}
