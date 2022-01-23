package com.kappacrypto.Clients;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kappacrypto.Models.Crypto;
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

    @Autowired
    ObjectMapper objectMapper;

    public CoinAPI(HttpClient httpClient) {
        this.client = httpClient;
    }

    public List<Crypto> getAssets() throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<InputStream> res = requestAssets();
        return handleResponse(res);
    }

    private List<Crypto> handleResponse(HttpResponse<InputStream> responseStream) throws IOException {
        // TODO: check content is gzip
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

}
