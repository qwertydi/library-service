package com.dmsc.openlibraryapi.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class OpenLibraryServerSdkException extends RuntimeException {

    private String errorCode;

    public OpenLibraryServerSdkException(String message) {
        super(message);
    }

    public OpenLibraryServerSdkException(String message, Throwable cause) {
        super(message, cause);
    }

    public OpenLibraryServerSdkException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
