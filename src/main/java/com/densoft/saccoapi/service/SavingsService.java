package com.densoft.saccoapi.service;

import com.densoft.saccoapi.dto.request.CreateTransactionReq;
import com.densoft.saccoapi.dto.response.AllCustomersTotalSavings;
import com.densoft.saccoapi.dto.response.CreateTransactionRes;
import com.densoft.saccoapi.dto.response.CustomerSavingsRes;

public interface SavingsService {
    CreateTransactionRes createTransaction(CreateTransactionReq createTransactionReq);

    AllCustomersTotalSavings getAllCustomersTotalSavings();

    CustomerSavingsRes getCustomerSavings(int memberNumber);


}
