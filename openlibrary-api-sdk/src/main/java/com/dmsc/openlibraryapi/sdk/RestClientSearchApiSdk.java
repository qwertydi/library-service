package com.dmsc.openlibraryapi.sdk;

import com.dmsc.openlibraryapi.model.SearchSdkRequest;
import com.dmsc.openlibraryapi.model.SearchSdkResponse;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import java.util.Optional;

public class RestClientSearchApiSdk implements SearchApiSdk {

    private final RestClient restclient;

    public RestClientSearchApiSdk(RestClient.Builder restClient) {
        this.restclient = restClient.build();
    }

    @Override
    public SearchSdkResponse searchBooks(SearchSdkRequest request) {
        Assert.notNull(request.getTitle(), "'title' cannot be null");

        RestClient.RequestHeadersSpec<?> uri = this.restclient.get()
            .uri(uriBuilder -> {
                    UriBuilder builder = uriBuilder
                        .path("search.json")
                        .queryParam("title", request.getTitle()
                            .replace(" ", "+"));
                    Optional.ofNullable(request.getOffset())
                        .ifPresent(offset -> builder.queryParam("offset", offset));
                    Optional.ofNullable(request.getPage())
                        .ifPresent(page -> builder.queryParam("page", page));
                    return builder.build();
                }
            );
        return uri
            .retrieve()
            .body(SearchSdkResponse.class);
    }
}
