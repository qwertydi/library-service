package com.dmsc.openlibraryapi.sdk;

import com.dmsc.openlibraryapi.model.SearchSdkRequest;
import com.dmsc.openlibraryapi.model.SearchSdkResponse;

public interface SearchApiSdk {
    SearchSdkResponse searchBooks(SearchSdkRequest request);
}
