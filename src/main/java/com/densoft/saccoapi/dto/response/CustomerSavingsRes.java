package com.densoft.saccoapi.dto.response;

import com.densoft.saccoapi.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class CustomerSavingsRes {
    private final Customer customer;

    private final Map<String, Double> savingsSummary;

    private final Double totalSavings;
}
