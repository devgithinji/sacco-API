package com.densoft.saccoapi.controller;

import com.densoft.saccoapi.dto.request.CreateTransactionReq;
import com.densoft.saccoapi.dto.response.AllCustomersTotalSavings;
import com.densoft.saccoapi.dto.response.CreateTransactionRes;
import com.densoft.saccoapi.dto.response.CustomerSavingsRes;
import com.densoft.saccoapi.service.SavingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/saving")
public class SavingController {

    private final SavingsService savingsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateTransactionRes saveTransaction(@Valid @RequestBody CreateTransactionReq createTransactionReq) {
        return savingsService.createTransaction(createTransactionReq);
    }

    @GetMapping
    public AllCustomersTotalSavings allCustomersTotalSavings() {
        return savingsService.getAllCustomersTotalSavings();
    }

    @GetMapping("customer/{memberNumber}")
    public CustomerSavingsRes customerSavingsRes(@PathVariable("memberNumber") int memberNumber) {
        return savingsService.getCustomerSavings(memberNumber);
    }
}
