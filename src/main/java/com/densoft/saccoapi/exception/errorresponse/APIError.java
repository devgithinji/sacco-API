package com.densoft.saccoapi.exception.errorresponse;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Getter
public class APIError {
    private final HttpStatusCode statusCode;
    private final String details;
    private final LocalDateTime timestamp;

    public APIError(HttpStatusCode statusCode, String details) {
        this.statusCode = statusCode;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }
}
