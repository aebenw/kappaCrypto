package com.kappacrypto.Configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kappacrypto.Clients.CoinAPI;
import com.kappacrypto.Clients.Twitter;
import com.kappacrypto.Models.Crypto;
import com.kappacrypto.utils.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;

@Service
@Slf4j
public class KappaService {

    @Autowired private ApplicationContext applicationContext;
    @Autowired private CoinAPI coinAPI;
    @Autowired private Twitter twitterClient;
    @Autowired private ObjectMapper objectMapper;

    List<Crypto> cryptoAssets;

    @PostConstruct
    public void fireUpService() throws URISyntaxException, IOException, InterruptedException {
        // Ensure coin api client has pullied most up to date coin data
        try {
            cryptoAssets = coinAPI.getAssets();
        } catch (IOException e) {
            log.error("Unable to get assets from CoinAPI:\n" + e);
        }

//        List<String> cryptoAccounts;
//        try {
//            cryptoAccounts = getCryptoAccounts();
//        } catch (Exception e) {
//            log.error("Unable to load account names from resouce dir", e);
//        }
    }

    private List<String> getCryptoAccounts() throws IOException {
        InputStream accountNamesStream = getClass().getClassLoader().getResourceAsStream("Twitter/CryptoAccounts.json");
        String content = StreamUtils.convertBufferToString(accountNamesStream);
        TypeReference<List<String>> accountType = new TypeReference<>() {};
        return objectMapper.readValue(content, accountType);
    }
}
