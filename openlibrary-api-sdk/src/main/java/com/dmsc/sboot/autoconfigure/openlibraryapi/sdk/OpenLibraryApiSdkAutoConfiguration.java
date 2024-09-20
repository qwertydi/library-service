package com.dmsc.sboot.autoconfigure.openlibraryapi.sdk;

import com.dmsc.openlibraryapi.sdk.RestClientSearchApiSdk;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Lazy
@Configuration
public class OpenLibraryApiSdkAutoConfiguration {
    private static final String PROPERTIES_PREFIX = "sdk.openlibrary-api";

    @Bean
    @Validated
    @ConfigurationProperties(PROPERTIES_PREFIX)
    public RestClientProperties openLibraryRestClientProperties() {
        return new RestClientProperties();
    }

    @Bean
    public RestClientSearchApiSdk restClientSearchApiSdk(
        RestClientProperties ryanairServiceProperties) {
        RequestConfig.Builder requestConfig = RequestConfig.copy(RequestConfig.DEFAULT);

        Optional.ofNullable(ryanairServiceProperties.getRequestConnectionTimeout())
            .ifPresent(requestConfig::setConnectionRequestTimeout);

        Optional.ofNullable(ryanairServiceProperties.getResponseTimeout())
            .ifPresent(requestConfig::setResponseTimeout);

        CloseableHttpClient httpClient = HttpClients.custom()
            .setDefaultRequestConfig(requestConfig.build())
            .build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);

        RestClient.Builder restClientBuilder = RestClient.builder()
            .requestFactory(factory)
            .baseUrl(ryanairServiceProperties.getBaseUrl())
            .build();

        return new RestClientSearchApiSdk(restClientBuilder);
    }
}
