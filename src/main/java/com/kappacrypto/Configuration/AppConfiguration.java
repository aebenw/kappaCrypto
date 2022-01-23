package com.kappacrypto.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.kappacrypto.Clients.Twitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.net.http.HttpClient;
import java.util.concurrent.Executor;


@Configuration
@EnableScheduling
public class AppConfiguration {
    @Value("${event.enrichment.thread.core.pool.size:10}")
    private int corePoolSize;

    @Value("${event.enrichment.thread.max.pool.size:60}")
    private int maxPoolSize;

    @Value("${event.enrichment.thread.pool.queue.capacity:1000}")
    private int threadPoolQueueCapacity;

    @Value("${event.enrichment.rate.limiter.permit.value:1}")
    private double rateLimiterPermitValue;

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newBuilder().build();
    }

    @Bean(name = "asyncExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(threadPoolQueueCapacity);
        executor.setThreadNamePrefix("KappaExecutor-");
        executor.initialize();
        return executor;
    }

    @Bean
    public Twitter twitterClient(@Autowired ObjectMapper objectMapper) {
        Twitter twitterClient = new Twitter();
        twitterClient.setObjectMapper(objectMapper);
        return twitterClient;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_ABSENT);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new Jdk8Module());

        return objectMapper;
    }

}
