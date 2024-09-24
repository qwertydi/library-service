package com.dmsc.openlibraryapi.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class OpenLibraryClientSdkException extends RuntimeException {

    private String errorCode;
    
    public OpenLibraryClientSdkException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
