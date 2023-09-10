package com.densoft.saccoapi.service.impl;

import com.densoft.saccoapi.dto.request.CreateSavingProductReq;
import com.densoft.saccoapi.dto.response.CreateSavingProductRes;
import com.densoft.saccoapi.exception.APIException;
import com.densoft.saccoapi.exception.ResourceNotFoundException;
import com.densoft.saccoapi.model.ActivationStatus;
import com.densoft.saccoapi.model.SavingProduct;
import com.densoft.saccoapi.repository.SavingProductRepository;
import com.densoft.saccoapi.service.SavingProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SavingProductServiceImpl implements SavingProductService {

    private final SavingProductRepository savingProductRepository;

    @Override
    public CreateSavingProductRes createSavingProduct(CreateSavingProductReq createSavingProductReq) {
        Optional<SavingProduct> savingProductOptional = savingProductRepository.findByName(createSavingProductReq.getName());
        if (savingProductOptional.isPresent()) throw new APIException("savings product with similar name exists");
        SavingProduct savingProduct = new SavingProduct(
                createSavingProductReq.getName(),
                createSavingProductReq.getDescription(),
                Double.parseDouble(createSavingProductReq.getInterestRate()),
                ActivationStatus.ACTIVE
        );

        SavingProduct savedProduct = savingProductRepository.save(savingProduct);

        return new CreateSavingProductRes("saving product created successfully", savedProduct);
    }

    @Override
    public SavingProduct findSavingProductById(long savingProductId) {
        return savingProductRepository.findById(savingProductId)
                .orElseThrow(() -> new ResourceNotFoundException("saving product", "Id", String.valueOf(savingProductId)));
    }

    @Override
    public List<SavingProduct> getAllSavingProducts() {
        return savingProductRepository.findAll();
    }

    @Override
    public CreateSavingProductRes updateSavingProduct(CreateSavingProductReq createSavingProductReq, long savingProductId) {
        SavingProduct savingProduct = savingProductRepository.findById(savingProductId)
                .orElseThrow(() -> new ResourceNotFoundException("saving product", "Id", String.valueOf(savingProductId)));

        Optional<SavingProduct> collidingSavingProduct = savingProductRepository.findByNameExcludeCurrentId(
                createSavingProductReq.getName(), String.valueOf(savingProductId));

        if (collidingSavingProduct.isPresent()) throw new APIException("name is already taken");

        savingProduct.setName(createSavingProductReq.getName());
        savingProduct.setDescription(createSavingProductReq.getDescription());
        savingProduct.setInterestRate(Double.parseDouble(createSavingProductReq.getInterestRate()));

        SavingProduct updatedSavingProduct = savingProductRepository.save(savingProduct);

        return new CreateSavingProductRes("saving product updated successfully", updatedSavingProduct);
    }

    @Override
    public String activateSavingProduct(long savingProductId) {
        return toggleSavingProductStatus(savingProductId, ActivationStatus.ACTIVE);
    }


    @Override
    public String deactivateSavingProduct(long savingProductId) {
        return toggleSavingProductStatus(savingProductId, ActivationStatus.DEACTIVATED);
    }


    private String toggleSavingProductStatus(long savingProductId, ActivationStatus activationStatus) {
        SavingProduct savingProduct = savingProductRepository.findById(savingProductId)
                .orElseThrow(() -> new ResourceNotFoundException("saving product", "Id", String.valueOf(savingProductId)));
        if (savingProduct.getActivationStatus().equals(activationStatus))
            throw new APIException("product %s is already %s".formatted(savingProduct.getName(), activationStatus.equals(ActivationStatus.ACTIVE) ? "active" : "deactivated"));
        savingProduct.setActivationStatus(activationStatus);
        savingProductRepository.save(savingProduct);
        return "saving product: %s %s".formatted(savingProduct.getName(), activationStatus.equals(ActivationStatus.ACTIVE) ? "activated" : "deactivated");
    }
}
