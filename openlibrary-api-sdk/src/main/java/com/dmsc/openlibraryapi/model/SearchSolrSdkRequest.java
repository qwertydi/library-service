package com.dmsc.openlibraryapi.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@ToString(callSuper = true)
public class SearchSolrSdkRequest extends SearchSdkRequest {
    private @NotNull String query;
}
