package com.densoft.saccoapi.controller;

import com.densoft.saccoapi.dto.request.CreateCustomerReq;
import com.densoft.saccoapi.dto.request.DeactivateCustomer;
import com.densoft.saccoapi.dto.response.CreateCustomerRes;
import com.densoft.saccoapi.exception.errorresponse.GeneralAPIError;
import com.densoft.saccoapi.exception.errorresponse.ValidationAPIError;
import com.densoft.saccoapi.model.Customer;
import com.densoft.saccoapi.service.CustomerService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@Tag(name = "Customer Management", description = "All customer management endpoints")
public class CustomerController {

    public final CustomerService customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new customer", description = "Creates a new customer and returns the created customer")
    @ApiResponse(responseCode = "201", description = "Customer Create",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateCustomerRes.class)))
    @ApiResponse(responseCode = "400", description = "customer with existing details exist",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralAPIError.class)))
    @ApiResponse(responseCode = "422", description = "invalid customer create details",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationAPIError.class)))
    public CreateCustomerRes createCustomer(@Valid @RequestBody CreateCustomerReq createCustomerReq) {
        return customerService.saveCustomer(createCustomerReq);
    }

    @GetMapping
    @Operation(summary = "Get Customers List", description = "Get a list of customers")
    public List<Customer> getCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping("/{customerId}")
    @Operation(summary = "Get Customer By Id", description = "Get customer by Id")
    @ApiResponse(responseCode = "200", description = "Get Customer by Id",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class)))
    @ApiResponse(responseCode = "404", description = "Customer not found with this Id",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralAPIError.class)))
    public Customer getCustomerById(@Parameter(name = "customerId", description = "Customer Id", in = ParameterIn.PATH, required = true,
            schema = @Schema(type = "integer")) @PathVariable("customerId") Long customerId) {
        return customerService.getCustomerById(customerId);
    }

    @PutMapping("/{customerId}")
    @Operation(summary = "Update Customer details", description = "Update Customer details")
    @ApiResponse(responseCode = "200", description = "Customer Update",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateCustomerRes.class)))
    @ApiResponse(responseCode = "404", description = "Customer not found with this Id",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralAPIError.class)))
    @ApiResponse(responseCode = "400", description = "Another customer with existing details exist",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralAPIError.class)))
    @ApiResponse(responseCode = "422", description = "invalid customer update details",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationAPIError.class)))
    public CreateCustomerRes updateCustomer(@Valid @RequestBody CreateCustomerReq createCustomerReq,
                                            @Parameter(name = "customerId", description = "Customer Id", in = ParameterIn.PATH, required = true,
                                                    schema = @Schema(type = "integer")) @PathVariable("customerId") Long customerId) {
        return customerService.updateCustomer(customerId, createCustomerReq);
    }

    @PostMapping("/deactivate/{customerId}")
    @Operation(summary = "Deactivate Customer", description = "Deactivate Customer")
    @ApiResponse(responseCode = "200", description = "Customer deactivation",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "Customer not found with this Id",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralAPIError.class)))
    public ResponseEntity<Map<String, String>> deactivateCustomer(@Valid @RequestBody DeactivateCustomer deactivateCustomer,
                                                                  @Parameter(name = "customerId", description = "Customer Id", in = ParameterIn.PATH, required = true,
                                                                          schema = @Schema(type = "integer")) @PathVariable("customerId") Long customerId) {
        String response = customerService.deactivateAccount(customerId, deactivateCustomer.getMessage());

        return ResponseEntity.ok(Map.of("message", response));
    }


    @PostMapping("/activate/{customerId}")
    @Operation(summary = "Activate Customer", description = "Activate Customer")
    @ApiResponse(responseCode = "200", description = "Customer activation",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "Customer not found with this Id",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralAPIError.class)))
    public ResponseEntity<Map<String, String>> activateCustomer(@Parameter(name = "customerId", description = "Customer Id", in = ParameterIn.PATH, required = true,
            schema = @Schema(type = "integer")) @PathVariable("customerId") Long customerId) {
        String response = customerService.activateAccount(customerId);

        return ResponseEntity.ok(Map.of("message", response));
    }


}
