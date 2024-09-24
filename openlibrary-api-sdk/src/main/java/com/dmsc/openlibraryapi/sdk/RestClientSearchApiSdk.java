package com.dmsc.openlibraryapi.sdk;

import com.dmsc.openlibraryapi.model.SearchSdkRequest;
import com.dmsc.openlibraryapi.model.SearchSdkResponse;
import com.dmsc.openlibraryapi.model.SearchSolrSdkRequest;
import com.dmsc.openlibraryapi.model.SearchTitleSdkRequest;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.Optional;

public class RestClientSearchApiSdk implements SearchApiSdk {

    private final RestClient restclient;

    public RestClientSearchApiSdk(RestClient.Builder restClient) {
        this.restclient = restClient.build();
    }

    /**
     * Constructs a URI for searching based on the provided search request and URI builder.
     * <p>
     * This method processes the given {@code SearchSdkRequest} to append various query parameters
     * to the URI based on the specific type of request (either {@code SearchSolrSdkRequest}
     * or {@code SearchTitleSdkRequest}). It handles optional parameters such as query, fields,
     * offset, page, and limit.
     *
     * @param request    The {@code SearchSdkRequest} containing search parameters.
     * @param uriBuilder The {@code UriBuilder} used to construct the URI.
     * @return A {@code URI} object constructed with the specified search parameters.
     * @throws IllegalArgumentException If the request type is unsupported.
     */
    private static URI getUri(SearchSdkRequest request, UriBuilder uriBuilder) {
        UriBuilder builder = uriBuilder
            .path("search.json");

        if (request instanceof SearchSolrSdkRequest solrSdkRequest) {
            Optional.ofNullable(solrSdkRequest.getQuery())
                .ifPresent(title -> builder.queryParam("q", solrSdkRequest.getQuery()
                    .replace(" ", "+")));
        } else if (request instanceof SearchTitleSdkRequest titleSdkRequest) {
            Optional.of(titleSdkRequest.getTitle())
                .ifPresent(title -> builder.queryParam("title", titleSdkRequest.getTitle()
                    .replace(" ", "+")));
        } else {
            throw new IllegalArgumentException("Unsupported Request Type");
        }

        Optional.ofNullable(request.getOffset())
            .ifPresent(offset -> builder.queryParam("offset", offset));
        Optional.ofNullable(request.getPage())
            .ifPresent(page -> builder.queryParam("page", page));
        Optional.ofNullable(request.getLimit())
            .ifPresent(limit -> builder.queryParam("limit", limit));

        return builder.build();
    }

    @Override
    public SearchSdkResponse searchBooks(SearchSdkRequest request) {
        RestClient.RequestHeadersSpec<?> uri = this.restclient.get()
            .uri(uriBuilder -> getUri(request, uriBuilder));
        return uri
            .retrieve()
            .body(SearchSdkResponse.class);
    }
}
