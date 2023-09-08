package com.densoft.saccoapi.exception;

import com.densoft.saccoapi.exception.errorresponse.APIError;
import com.densoft.saccoapi.exception.errorresponse.GeneralAPIError;
import com.densoft.saccoapi.exception.errorresponse.ValidationAPIError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIError> handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException, WebRequest webRequest) {
        APIError apiError = new GeneralAPIError(
                HttpStatus.NOT_FOUND,
                webRequest.getDescription(false),
                resourceNotFoundException.getMessage()
        );
        return ResponseEntity.status(apiError.getStatusCode()).body(apiError);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIError> handleAPIException(APIException apiException, WebRequest webRequest) {
        APIError apiError = new GeneralAPIError(
                HttpStatus.BAD_REQUEST,
                webRequest.getDescription(false),
                apiException.getMessage()
        );
        return ResponseEntity.status(apiError.getStatusCode()).body(apiError);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> errors = ex.getBindingResult().getAllErrors().stream()
                .filter(FieldError.class::isInstance) // Filter only FieldErrors
                .map(objectError -> {
                    FieldError fieldError = (FieldError) objectError;
                    String fieldName = fieldError.getField();
                    String message = fieldError.getDefaultMessage();
                    return new AbstractMap.SimpleEntry<>(fieldName, message);
                })
                .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        APIError apiError = new ValidationAPIError(HttpStatus.UNPROCESSABLE_ENTITY, request.getDescription(false), errors);

        return ResponseEntity.status(apiError.getStatusCode()).body(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        APIError apiError = new GeneralAPIError(statusCode, request.getDescription(false), ex.getLocalizedMessage());
        return ResponseEntity.status(statusCode).body(apiError);
    }

}
