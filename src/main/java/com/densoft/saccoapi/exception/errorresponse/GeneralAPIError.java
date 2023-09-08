package com.densoft.saccoapi.exception.errorresponse;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class GeneralAPIError extends APIError {
    private final String error;

    public GeneralAPIError(HttpStatusCode statusCode, String details, String error) {
        super(statusCode, details);
        this.error = error;
    }
}
