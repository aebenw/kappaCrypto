package com.kappacrypto.Clients;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kappacrypto.Models.Crypto;
import com.kappacrypto.utils.Sorters.AssetsByDayTradingVolume;
import com.kappacrypto.utils.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class CoinAPI {

    HttpClient client;
    @Value("${client.coinapi.endpoint.baseurl}")
    String baseUrl;
    @Value("${client.coinapi.endpoint.assets}")
    String assetUrl;
    @Value("${client.coinapi.endpoint.assets.icons}")
    String iconUrl;
    @Value("${client.coinapi.privatekey}")
    private String privateKey;
    @Value("${client.coinapi.useLocalFile}")
    private boolean useLocalFile;

    @Autowired
    ObjectMapper objectMapper;

    private List<Crypto> assets = new ArrayList<>();
    private List<Crypto> topAssetsByDay = new ArrayList<>();

    public CoinAPI(HttpClient httpClient) {
        this.client = httpClient;
    }

    public List<Crypto> getAllAssets() throws URISyntaxException, IOException, InterruptedException {
        List<Crypto> fullAssetList;
        if (useLocalFile) {
            TypeReference<List<Crypto>> tr = new TypeReference<>(){};
            InputStream cyptoAssetStream = getClass().getClassLoader().getResourceAsStream("Twitter/cryptoAssets.json");
            fullAssetList = new ArrayList<>(objectMapper.readValue(
                    StreamUtils.convertBufferToString(cyptoAssetStream), tr
            ));
        } else {
            HttpResponse<InputStream> res = requestAssets();
            fullAssetList = handleResponse(res);
        }
        cleanAssets(fullAssetList);
        return assets;

    }

    private List<Crypto> handleResponse(HttpResponse<InputStream> responseStream) throws IOException {
        String unzippedRes = StreamUtils.convertGzipResponseToString(responseStream.body());

        log.info("unzippedRes:\n " + unzippedRes);
        TypeReference<List<Crypto>> tr = new TypeReference<List<Crypto>>() {};
        List<Crypto> assets = objectMapper.readValue(unzippedRes, tr);
        return assets;
    }

    private void getAssetIcons(Crypto c) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseUrl + iconUrl + "{iconSize}")) // TODO: Get Icons by size
                .header("X-CoinAPI-Key", privateKey)
                .header("Accept", "application/json")
                .build();
        HttpResponse<InputStream> res =  client.send(request, HttpResponse.BodyHandlers.ofInputStream());
        handleResponse(res);
    }

    private HttpResponse<InputStream> requestAssets() throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseUrl + assetUrl))
                .header("X-CoinAPI-Key", privateKey)
                .header("Accept", "application/json")
                .header("Accept-Encoding", "deflate, gzip")
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofInputStream());
    }

    public List<Crypto> topAssetsByDayTradingVolume() throws URISyntaxException, IOException, InterruptedException {
        // Twitter streams allows for only 25 rules so returning top 25 by trading volume
        if (assets.isEmpty()) getAllAssets();
        Collections.sort(this.assets, new AssetsByDayTradingVolume());
        for (int i = 0; i <=25; i++) this.topAssetsByDay.add(this.assets.get(i));

        return this.topAssetsByDay;
    }

    public void cleanAssets(List<Crypto> fullAssetList) throws IOException {
        for (Crypto crypto: fullAssetList) {
            if (crypto.volume1DayUsd > 0 && crypto.typeIsCrypto == 1) assets.add(crypto);
        }
    }

}
