package com.densoft.saccoapi.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateSavingProductReq {
    @NotBlank(message = "name required")
    private String name;
    @NotBlank(message = "description required")
    private String description;
    @NotBlank(message = "interest Rate required")
    @DecimalMin(value = "0.0", message = "interest rate cannot be greater than 0")
    private double interestRate;
}
