package com.densoft.saccoapi.dto.response;

import com.densoft.saccoapi.model.SavingProduct;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateSavingProductRes {
    private final String message;
    private final SavingProduct savingProduct;
}

