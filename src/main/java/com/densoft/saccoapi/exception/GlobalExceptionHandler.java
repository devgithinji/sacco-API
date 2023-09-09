package com.densoft.saccoapi.exception;

import com.densoft.saccoapi.exception.errorresponse.APIError;
import com.densoft.saccoapi.exception.errorresponse.GeneralAPIError;
import com.densoft.saccoapi.exception.errorresponse.ValidationAPIError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIError> handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException, WebRequest webRequest) {
        APIError apiError = new GeneralAPIError(
                HttpStatus.NOT_FOUND,
                webRequest.getDescription(false),
                resourceNotFoundException.getMessage()
        );
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIError> handleAPIException(APIException apiException, WebRequest webRequest) {
        APIError apiError = new GeneralAPIError(
                HttpStatus.BAD_REQUEST,
                webRequest.getDescription(false),
                apiException.getMessage()
        );
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        Map<String, List<String>> errors = getErrors(ex);

        APIError apiError = new ValidationAPIError(HttpStatus.UNPROCESSABLE_ENTITY, request.getDescription(false), errors);

        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    private static Map<String, List<String>> getErrors(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getAllErrors().stream()
                .filter(FieldError.class::isInstance)
                .map(objectError -> {
                    FieldError fieldError = (FieldError) objectError;
                    String fieldName = fieldError.getField();
                    String message = fieldError.getDefaultMessage();
                    return new AbstractMap.SimpleEntry<>(fieldName, message);
                })
                .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty())
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleOtherExceptions(Exception ex, WebRequest request) {
        APIError apiError = new GeneralAPIError(HttpStatus.INTERNAL_SERVER_ERROR,
                request.getDescription(false),
                ex.getLocalizedMessage());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

}
