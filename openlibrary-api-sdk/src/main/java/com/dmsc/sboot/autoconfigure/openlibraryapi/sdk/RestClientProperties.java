package com.dmsc.sboot.autoconfigure.openlibraryapi.sdk;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.apache.hc.core5.util.Timeout;

@Data
public class RestClientProperties {
    private @NotNull String baseUrl;
    private Timeout requestConnectionTimeout;
    private Timeout responseTimeout;
}
