package com.dmsc.openlibraryapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SearchSdkResponse {
    private int start;
    @JsonProperty("numFound")
    private int numFound2;
    @JsonProperty("num_found")
    private int numFound;
    private List<BookSdk> docs = new ArrayList<>();
    @JsonProperty("offset")
    private int offset;
}
