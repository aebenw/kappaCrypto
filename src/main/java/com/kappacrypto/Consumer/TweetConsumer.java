package com.kappacrypto.Consumer;

import io.github.redouane59.twitter.dto.tweet.Tweet;

import java.util.function.Consumer;

public class TweetConsumer implements Consumer<Tweet> {
    @Override
    public void accept(Tweet tweet) {

    }

    @Override
    public Consumer<Tweet> andThen(Consumer<? super Tweet> after) {
        return Consumer.super.andThen(after);
    }
}
