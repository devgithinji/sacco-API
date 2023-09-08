package com.densoft.saccoapi.service;

import com.densoft.saccoapi.dto.request.CreateSavingProductReq;
import com.densoft.saccoapi.dto.response.CreateSavingProductRes;
import com.densoft.saccoapi.model.SavingProduct;

import java.util.List;

public interface SavingProductService {

    CreateSavingProductRes createSavingProduct(CreateSavingProductReq createSavingProductReq);

    SavingProduct findSavingProductById(long savingProductId);

    List<SavingProduct> getAllSavingProducts();

    CreateSavingProductRes updateSavingProduct(CreateSavingProductReq createSavingProductReq, long savingProductId);

    String activateSavingProduct(long savingProductId);

    String deactivateSavingProduct(long savingProductId);


}
