package me.jander.services;

import me.jander.models.Greeting;

public class FakeReviewService implements ReviewService {
    @Override
    public Greeting getGreeting() {
        return new Greeting(81, "Foo");
    }
}
