package com.densoft.saccoapi.controller;

import com.densoft.saccoapi.dto.request.CreateSavingProductReq;
import com.densoft.saccoapi.dto.response.CreateSavingProductRes;
import com.densoft.saccoapi.model.SavingProduct;
import com.densoft.saccoapi.service.SavingProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/saving-product")
@AllArgsConstructor
public class SavingProductController {

    private final SavingProductService savingProductService;

    @PostMapping
    public CreateSavingProductRes createSavingProduct(@Valid @RequestBody CreateSavingProductReq createSavingProductReq) {
        return savingProductService.createSavingProduct(createSavingProductReq);
    }

    @GetMapping("/{productId}")
    public SavingProduct getSavingProductById(@PathVariable("productId") long productId) {
        return savingProductService.findSavingProductById(productId);
    }

    @GetMapping
    public List<SavingProduct> getAllSavingProducts() {
        return savingProductService.getAllSavingProducts();
    }

    @PutMapping("/{productId}")
    public CreateSavingProductRes createSavingProduct(@Valid @RequestBody CreateSavingProductReq createSavingProductReq,
                                                      @PathVariable("productId") long productId) {
        return savingProductService.updateSavingProduct(createSavingProductReq, productId);
    }

    @PostMapping("activate/{productId}")
    public ResponseEntity<Map<String, String>> activateProduct(@PathVariable("productId") long productId) {
        String response = savingProductService.activateSavingProduct(productId);
        return ResponseEntity.ok(Map.of("message", response));
    }

    @PostMapping("deactivate/{productId}")
    public ResponseEntity<Map<String, String>> deactivateProduct(@PathVariable("productId") long productId) {
        String response = savingProductService.deactivateSavingProduct(productId);
        return ResponseEntity.ok(Map.of("message", response));
    }
}
