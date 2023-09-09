package com.densoft.saccoapi.exception.errorresponse;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Getter
public class APIError {
    private final HttpStatusCode status;
    private final String details;
    private final LocalDateTime timestamp;

    public APIError(HttpStatusCode status, String details) {
        this.status = status;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }
}
