package com.dmsc.openlibraryapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BookSdk {

    @JsonProperty("title")
    private String title;

    @JsonProperty("author_name")
    private List<String> authorName;

    @JsonProperty("first_publish_year")
    private int firstPublishYear;

    @JsonProperty("key")
    private String key;

    @JsonProperty("author_key")
    private List<String> authorKey;

    private List<String> language;
}
