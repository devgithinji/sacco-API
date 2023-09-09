package com.densoft.saccoapi.dto.response;

import com.densoft.saccoapi.model.Transaction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateTransactionRes {
    private String message;
    @JsonIgnoreProperties(value = {"customer", "savingProduct"})
    private Transaction transaction;

}
