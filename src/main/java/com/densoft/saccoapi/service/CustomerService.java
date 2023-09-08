package com.densoft.saccoapi.service;

import com.densoft.saccoapi.dto.request.CreateCustomerReq;
import com.densoft.saccoapi.dto.response.CreateCustomerRes;
import com.densoft.saccoapi.model.Customer;

import java.util.List;

public interface CustomerService {

    List<Customer> getCustomers();

    Customer getCustomerById(long customerId);

    CreateCustomerRes saveCustomer(CreateCustomerReq createCustomerReq);

    CreateCustomerRes updateCustomer(long customerId, CreateCustomerReq createCustomerReq);

    String deactivateAccount(long customerId, String deactivationReason);

    String activateAccount(long customerId);


}
