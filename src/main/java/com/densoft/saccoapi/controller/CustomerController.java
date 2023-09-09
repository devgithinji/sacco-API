package com.densoft.saccoapi.controller;

import com.densoft.saccoapi.dto.request.CreateCustomerReq;
import com.densoft.saccoapi.dto.request.DeactivateCustomer;
import com.densoft.saccoapi.dto.response.CreateCustomerRes;
import com.densoft.saccoapi.model.Customer;
import com.densoft.saccoapi.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    public final CustomerService customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateCustomerRes createCustomer(@Valid @RequestBody CreateCustomerReq createCustomerReq) {
        return customerService.saveCustomer(createCustomerReq);
    }

    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping("/{customerId}")
    public Customer getCustomerById(@PathVariable("customerId") Long customerId) {
        return customerService.getCustomerById(customerId);
    }

    @PutMapping("/{customerId}")
    public CreateCustomerRes updateCustomer(@Valid @RequestBody CreateCustomerReq createCustomerReq,
                                            @PathVariable("customerId") Long customerId) {
        return customerService.updateCustomer(customerId, createCustomerReq);
    }

    @PostMapping("/deactivate/{customerId}")
    public ResponseEntity<Map<String, String>> deactivateCustomer(@Valid @RequestBody DeactivateCustomer deactivateCustomer,
                                                                  @PathVariable("customerId") Long customerId) {
        String response = customerService.deactivateAccount(customerId, deactivateCustomer.getMessage());

        return ResponseEntity.ok(Map.of("message", response));
    }


    @PostMapping("/activate/{customerId}")
    public ResponseEntity<Map<String, String>> deactivateCustomer(@PathVariable("customerId") Long customerId) {
        String response = customerService.activateAccount(customerId);

        return ResponseEntity.ok(Map.of("message", response));
    }


}
