package com.dmsc.libraryserviceapi.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatusCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LibraryInvalidDataException extends RuntimeException {
    private final HttpStatusCode httpStatusCode;

    public LibraryInvalidDataException(String message, HttpStatusCode httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }
}
