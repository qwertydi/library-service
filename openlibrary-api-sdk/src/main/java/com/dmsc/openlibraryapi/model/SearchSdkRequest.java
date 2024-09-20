package com.dmsc.openlibraryapi.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SearchSdkRequest {
    private @NotNull String title;
    private Integer offset;
    private Integer page;
}
