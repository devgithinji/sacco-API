package com.densoft.saccoapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class AllCustomersTotalSavings {
    private final Map<String, Object> savingsSummary;
    private final Double totalSavings;
}
