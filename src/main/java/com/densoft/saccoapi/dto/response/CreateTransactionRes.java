package com.densoft.saccoapi.dto.response;

import com.densoft.saccoapi.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateTransactionRes {
    private String message;
    private Transaction transaction;
}
