package com.kappacrypto.Clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kappacrypto.Consumer.TweetConsumer;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.user.UserV2;
import io.github.redouane59.twitter.signature.TwitterCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Slf4j
public class Twitter {
    @Value("${client.twitter.publickey}")
    private String publicKey;
    @Value("${client.twitter.privatekey}")
    private String privateKey;
    @Value("${client.twitter.bearer.token}")
    private String bearerToken;

    private TwitterClient client;
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<String> cryptoAccounts;


    public Twitter() {
        TwitterCredentials credentials = TwitterCredentials.builder()
                .apiKey(publicKey)
                .apiSecretKey(privateKey)
                .bearerToken(bearerToken)
                .build();
        client = new TwitterClient(credentials);
    }

    public void getStreamRules() {
        client.retrieveFilteredStreamRules();
    }

//    public void deletetreamRules() {
//        client.deleteFilteredStreamRule();
//        client.deleteFilteredStreamRuleId();
//    }

    public void createStreamRules(String rule, String tag) {
        client.addFilteredStreamRule(rule, tag);
    }

    // Query for both abreviations and full names
        // hashtags and tweets themselves
    @Scheduled(fixedRate=1000)
    public void getTweets(String userName) {
        UserV2 user = client.getUserFromUserName(userName);
        System.out.println("user: " + user);
    }



    public void streamTweets() {
        client.startFilteredStream(new TweetConsumer());
    }
// Create List of accounts from json array


}
