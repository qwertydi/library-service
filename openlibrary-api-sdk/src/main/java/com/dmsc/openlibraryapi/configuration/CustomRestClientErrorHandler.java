package com.dmsc.openlibraryapi.configuration;

import com.dmsc.openlibraryapi.exception.OpenLibraryClientSdkException;
import com.dmsc.openlibraryapi.exception.OpenLibraryServerSdkException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * Custom Response Error Handler, that will convert responses different from 200 to {@link OpenLibraryClientSdkException}
 */
public class CustomRestClientErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        HttpStatusCode statusCode = httpResponse.getStatusCode();
        return (statusCode.is4xxClientError() || statusCode.is5xxServerError());
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        HttpStatusCode statusCode = httpResponse.getStatusCode();

        if (statusCode.is4xxClientError()) {
            // Handle 4xx errors
            throw new OpenLibraryClientSdkException(String.valueOf(statusCode.value()), "Client error occurred: " + statusCode);
        } else if (statusCode.is5xxServerError()) {
            // Handle 5xx errors
            throw new OpenLibraryServerSdkException(String.valueOf(statusCode.value()), "Server error occurred: " + statusCode);
        }
    }
}
