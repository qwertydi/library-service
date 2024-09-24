package com.dmsc.openlibraryapi.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@ToString(callSuper = true)
public class SearchTitleSdkRequest extends SearchSdkRequest {
    private @NotNull String title;
}
