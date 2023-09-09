package com.densoft.saccoapi.dto.request;

import com.densoft.saccoapi.exception.EnumValidator;
import com.densoft.saccoapi.model.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateTransactionReq {
    @EnumValidator(type = PaymentMethod.class)
    private String paymentMethod;
    @NotBlank(message = "amount is required")
    @Pattern(regexp = "^[0-9]*\\.?[0-9]*$", message = "amount must be a valid numeric value")
    @DecimalMin(value = "100", message = "amount must be greater than 100")
    private String amount;
    @NotNull(message = "customer member number is required")
    private String memberNumber;
    @NotNull(message = "saving product id required")
    private int savingProduct;

}
