package com.dmsc.openlibraryapi.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class SearchSdkRequest {
    private Integer offset;
    private Integer page;
    private Integer limit;
}
