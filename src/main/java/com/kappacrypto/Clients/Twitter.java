package com.kappacrypto.Clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kappacrypto.Consumer.TweetConsumer;
import com.kappacrypto.Models.Crypto;
import com.kappacrypto.utils.TwitterUtils;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.stream.StreamRules;
import io.github.redouane59.twitter.signature.TwitterCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

@Slf4j
@Component
public class Twitter {

    private String publicKey;
    private String privateKey;
    private String bearerToken;

    private TwitterClient client;
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<String> cryptoAccounts;
    private Map<String, StreamRules.StreamRule> existingStreamRules = new HashMap<>();


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
        if (existingStreamRules.isEmpty()) {
            List<StreamRules.StreamRule> streamRules = client.retrieveFilteredStreamRules();
            if (isNull(streamRules)) return;
            for (StreamRules.StreamRule streamRule: streamRules) {
                existingStreamRules.put(streamRule.getValue(), streamRule);
            }
        }
    }

    public void deleteStreamRules(StreamRules.StreamRule streamRule) {
        client.deleteFilteredStreamRuleId(streamRule.getId());
    }

    public void deleteAllStreamRules() {
        List<StreamRules.StreamRule> streamRules = client.retrieveFilteredStreamRules();
        streamRules.forEach((StreamRules.StreamRule sr) -> {
            client.deleteFilteredStreamRuleId(sr.getId());
        });
    }

    public void createStreamRules(String filter, String tag) {
        if (checkForStreamRule(filter)) return;
        try {
            StreamRules.StreamRule streamRule = client.addFilteredStreamRule(filter, tag);
        } catch (Exception e) {
            log.error("error creating rule: ", e);
        }

    }

    public void createRuleFromCrypto(List<Crypto> assets, String tag) {
        String filter = TwitterUtils.createRuleFromAssetName(assets);
        createStreamRules(filter, tag);
    }

    public boolean checkForStreamRule(String filter) {
        getStreamRules();
        if (existingStreamRules.containsKey(filter)) return true;
        return false;

    }

    public void streamTweets() {
        client.startFilteredStream(new TweetConsumer());
    }

}
