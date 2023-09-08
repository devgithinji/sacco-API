package com.densoft.saccoapi.dto.request;

import com.densoft.saccoapi.model.PaymentMethod;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTransactionReq {
    @NotNull(message = "payment method required")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @NotBlank(message = "amount is required")
    private Double amount;
    @NotNull(message = "customer id required")
    private int customer;
    @NotNull(message = "saving product id required")
    private int savingProduct;

}
