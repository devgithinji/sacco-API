package com.densoft.saccoapi.dto.response;

import com.densoft.saccoapi.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCustomerRes {
    private final String message;
    private final Customer customer;
}
