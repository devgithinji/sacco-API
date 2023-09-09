package com.densoft.saccoapi.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateSavingProductReq {
    @NotBlank(message = "name required")
    private String name;
    @NotBlank(message = "description required")
    private String description;
    @NotBlank(message = "interest Rate required")
    @Pattern(regexp = "^[0-9]*\\.?[0-9]*$", message = "Input must be a valid numeric value")
    @DecimalMin(value = "0.1", message = "Input must be greater than 0")
    private String interestRate;
}
