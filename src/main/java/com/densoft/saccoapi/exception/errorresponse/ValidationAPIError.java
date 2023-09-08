package com.densoft.saccoapi.exception.errorresponse;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import java.util.Map;

@Getter
public class ValidationAPIError extends APIError {
    private final Map<String, String> errors;

    public ValidationAPIError(HttpStatusCode statusCode, String details, Map<String, String> errors) {
        super(statusCode, details);
        this.errors = errors;
    }
}
