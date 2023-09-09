package com.densoft.saccoapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeactivateCustomer {
    @NotBlank(message = "message required")
    private String message;
}
