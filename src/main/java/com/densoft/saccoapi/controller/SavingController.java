package com.densoft.saccoapi.controller;

import com.densoft.saccoapi.dto.request.CreateTransactionReq;
import com.densoft.saccoapi.dto.response.AllCustomersTotalSavings;
import com.densoft.saccoapi.dto.response.CreateTransactionRes;
import com.densoft.saccoapi.dto.response.CustomerSavingsRes;
import com.densoft.saccoapi.exception.errorresponse.GeneralAPIError;
import com.densoft.saccoapi.exception.errorresponse.ValidationAPIError;
import com.densoft.saccoapi.service.SavingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/saving")
@Tag(name = "Savings Management", description = "All savings transactions endpoints")
public class SavingController {

    private final SavingsService savingsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Saving transaction", description = "Creates a new saving transaction")
    @ApiResponse(responseCode = "201", description = "Saving Transaction Create",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateTransactionRes.class)))
    @ApiResponse(responseCode = "422", description = "invalid saving create details",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationAPIError.class)))
    public CreateTransactionRes saveTransaction(@Valid @RequestBody CreateTransactionReq createTransactionReq) {
        return savingsService.createTransaction(createTransactionReq);
    }

    @GetMapping
    @Operation(summary = "All Customers Savings Summary", description = "Get all customers savings summary")
    @ApiResponse(responseCode = "200", description = "Get All Customer's Savings Transactions Summary",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerSavingsRes.class)))
    public AllCustomersTotalSavings allCustomersTotalSavings() {
        return savingsService.getAllCustomersTotalSavings();
    }

    @GetMapping("customer/{memberNumber}")
    @Operation(summary = "Customer Savings Summary", description = "Customer's savings summary")
    @ApiResponse(responseCode = "200", description = "Get Customer's Savings Transactions Summary",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerSavingsRes.class)))
    @ApiResponse(responseCode = "404", description = "Customer not found with this Id",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralAPIError.class)))
    public CustomerSavingsRes customerSavingsRes(@Parameter(name = "memberNumber", description = "member Number", in = ParameterIn.PATH, required = true,
            schema = @Schema(type = "integer")) @PathVariable("memberNumber") int memberNumber) {
        return savingsService.getCustomerSavings(memberNumber);
    }
}
