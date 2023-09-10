package com.densoft.saccoapi.controller;

import com.densoft.saccoapi.dto.request.CreateSavingProductReq;
import com.densoft.saccoapi.dto.response.CreateSavingProductRes;
import com.densoft.saccoapi.exception.errorresponse.GeneralAPIError;
import com.densoft.saccoapi.exception.errorresponse.ValidationAPIError;
import com.densoft.saccoapi.model.SavingProduct;
import com.densoft.saccoapi.service.SavingProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/saving-product")
@AllArgsConstructor
@Tag(name = "Saving Product Management", description = "All Saving Product management endpoints")
public class SavingProductController {

    private final SavingProductService savingProductService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new Savings Product", description = "Creates a new Savings Product and returns the Savings Product")
    @ApiResponse(responseCode = "201", description = "Savings Product Create",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateSavingProductRes.class)))
    @ApiResponse(responseCode = "400", description = "Savings product with existing details exist",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralAPIError.class)))
    @ApiResponse(responseCode = "422", description = "invalid savings product create details",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationAPIError.class)))
    public CreateSavingProductRes createSavingProduct(@Valid @RequestBody CreateSavingProductReq createSavingProductReq) {
        return savingProductService.createSavingProduct(createSavingProductReq);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get SavingsProduct By Id", description = "Get savings product by Id")
    @ApiResponse(responseCode = "200", description = "Get Savings Product by Id",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SavingProduct.class)))
    @ApiResponse(responseCode = "404", description = "Savings Product not found with this Id",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralAPIError.class)))
    public SavingProduct getSavingProductById(@Parameter(name = "productId", description = "SavingsProduct Id", in = ParameterIn.PATH, required = true,
            schema = @Schema(type = "integer")) @PathVariable("productId") long productId) {
        return savingProductService.findSavingProductById(productId);
    }

    @GetMapping
    @Operation(summary = "Get Savings Products List", description = "Get a list of Savings Product")
    public List<SavingProduct> getAllSavingProducts() {
        return savingProductService.getAllSavingProducts();
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Update Savings Product details", description = "Update Savings Product details")
    @ApiResponse(responseCode = "200", description = "Savings Product Update",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateSavingProductRes.class)))
    @ApiResponse(responseCode = "404", description = "Savings Product not found with this Id",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralAPIError.class)))
    @ApiResponse(responseCode = "400", description = "Another savings product with existing details exist",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralAPIError.class)))
    @ApiResponse(responseCode = "422", description = "invalid savings product update details",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationAPIError.class)))
    public CreateSavingProductRes updateSavingProduct(@Valid @RequestBody CreateSavingProductReq createSavingProductReq,
                                                      @Parameter(name = "productId", description = "SavingsProduct Id", in = ParameterIn.PATH, required = true,
                                                              schema = @Schema(type = "integer")) @PathVariable("productId") long productId) {
        return savingProductService.updateSavingProduct(createSavingProductReq, productId);
    }

    @PostMapping("activate/{productId}")
    @Operation(summary = "Activate Savings Product", description = "Activate Savings Product")
    @ApiResponse(responseCode = "200", description = "Savings activation",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "Savings Product not found with this Id",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralAPIError.class)))
    public ResponseEntity<Map<String, String>> activateProduct(@Parameter(name = "productId", description = "SavingsProduct Id", in = ParameterIn.PATH, required = true,
            schema = @Schema(type = "integer")) @PathVariable("productId") long productId) {
        String response = savingProductService.activateSavingProduct(productId);
        return ResponseEntity.ok(Map.of("message", response));
    }

    @PostMapping("deactivate/{productId}")
    @Operation(summary = "Deactivate Savings Product", description = "Deactivate Savings Product")
    @ApiResponse(responseCode = "200", description = "Savings deactivation",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "Savings Product not found with this Id",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralAPIError.class)))
    public ResponseEntity<Map<String, String>> deactivateProduct(@Parameter(name = "productId", description = "SavingsProduct Id", in = ParameterIn.PATH, required = true,
            schema = @Schema(type = "integer")) @PathVariable("productId") long productId) {
        String response = savingProductService.deactivateSavingProduct(productId);
        return ResponseEntity.ok(Map.of("message", response));
    }
}
