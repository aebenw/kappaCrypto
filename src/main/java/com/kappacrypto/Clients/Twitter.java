package com.kappacrypto.Clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kappacrypto.Consumer.TweetConsumer;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.stream.StreamRules;
import io.github.redouane59.twitter.signature.TwitterCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class Twitter {

    private String publicKey;
    private String privateKey;
    private String bearerToken;

    private TwitterClient client;
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<String> cryptoAccounts;


    public Twitter(
            @Value("${client.twitter.publickey}") String publicKey,
            @Value("${client.twitter.privatekey}") String privateKey,
            @Value("${client.twitter.bearer.token}") String bearerToken
    ) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.bearerToken = bearerToken;

        TwitterCredentials credentials = TwitterCredentials.builder()
                .apiKey(publicKey)
                .apiSecretKey(privateKey)
                .bearerToken(bearerToken)
                .build();
        client = new TwitterClient(credentials);
    }

    public void getStreamRules() {
        List<StreamRules.StreamRule> streamRules = client.retrieveFilteredStreamRules();
        System.out.println(streamRules);
    }

//    public void deletetreamRules() {
//        client.deleteFilteredStreamRule();
//        client.deleteFilteredStreamRuleId();
//    }

    public void createStreamRules(String rule, String tag) {
        //TODO: Add check to see if rule exists via tag or matching strings
        StreamRules.StreamRule streamRule = client.addFilteredStreamRule(rule, tag);
        System.out.println(streamRule);
    }

    public void streamTweets() {
        client.startFilteredStream(new TweetConsumer());
    }
// Create List of accounts from json array


}
