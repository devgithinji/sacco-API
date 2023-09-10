package com.densoft.saccoapi.service.impl;

import com.densoft.saccoapi.dto.request.CreateCustomerReq;
import com.densoft.saccoapi.dto.response.CreateCustomerRes;
import com.densoft.saccoapi.model.ActivationStatus;
import com.densoft.saccoapi.model.Customer;
import com.densoft.saccoapi.repository.CustomerRepository;
import com.densoft.saccoapi.service.NumberGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private NumberGenerator numberGenerator;
    @InjectMocks
    private CustomerServiceImpl customerService;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer(
                1,
                "John",
                "doe",
                345678,
                "john@gmail.com",
                "0767890456",
                234534534,
                ActivationStatus.ACTIVE);
    }

    @DisplayName("Get all customers test")
    @Test
    void getCustomers() {
        //given
        Customer customer2 = new Customer(
                2,
                "Jane",
                "doe",
                3434534,
                "jane@gmail.com",
                "0767098765",
                2345678,
                ActivationStatus.ACTIVE);
        List<Customer> newCustomers = List.of(customer, customer2);
        given(customerRepository.findAll()).willReturn(newCustomers);
        //when
        List<Customer> customers = customerService.getCustomers();
        //then
        assertEquals(customers.size(), newCustomers.size());
    }

    @DisplayName("Get customers by Id test")
    @Test
    void getCustomerById() {
        //given
        long customerId = 1L;
        given(customerRepository.findById(customerId)).willReturn(Optional.of(customer));
        //when
        Customer customer = customerService.getCustomerById(customerId);
        //then
        assertNotNull(customer);
    }

    @DisplayName("Save customer test")
    @Test
    void saveCustomer() {
        //given
        given(customerRepository.save(any(Customer.class))).willReturn(customer);
        given(numberGenerator.generateAccountNumber()).willReturn(679994557);
        CreateCustomerReq createCustomerReq = new CreateCustomerReq(
                customer.getFirstName(),
                customer.getLastName(),
                String.valueOf(customer.getIdNo()),
                customer.getEmail(),
                customer.getPhoneNumber()
        );
        given(customerRepository.findByEmailOrIdNoOrPhoneNumber(createCustomerReq.getEmail(),
                Integer.parseInt(createCustomerReq.getIdNo()),
                createCustomerReq.getPhoneNumber())).willReturn(Optional.empty());
        //when
        CreateCustomerRes createCustomerRes = customerService.saveCustomer(createCustomerReq);
        //then
        assertNotNull(createCustomerRes);
        assertTrue(createCustomerRes.getCustomer().getId() > 0);
    }

    @DisplayName("Update customer test")
    @Test
    void updateCustomer() {
        //given
        long customerId = 1L;
        given(customerRepository.findById(customerId)).willReturn(Optional.of(customer));
        String newEmail = "updated@email.com";
        CreateCustomerReq createCustomerReq = new CreateCustomerReq(
                customer.getFirstName(),
                customer.getLastName(),
                String.valueOf(customer.getIdNo()),
                newEmail,
                customer.getPhoneNumber()
        );
        given(customerRepository.findOtherCustomerWithSimilarDetailsExcludingCurrent(
                createCustomerReq.getEmail(),
                Integer.parseInt(createCustomerReq.getIdNo()),
                createCustomerReq.getPhoneNumber(),
                customerId)).willReturn(Optional.empty());
        given(customerRepository.save(customer)).willReturn(customer);
        //when
        CreateCustomerRes createCustomerRes = customerService.updateCustomer(customerId, createCustomerReq);
        //then
        assertNotNull(createCustomerRes);
        assertEquals(newEmail, createCustomerRes.getCustomer().getEmail());
    }

    @DisplayName("deactivate customer test")
    @Test
    void deactivateAccount() {
        //given
        long customerId = 1L;
        String deactivationReason = "dormant account";
        given(customerRepository.findById(customerId)).willReturn(Optional.of(customer));
        customer.setDeactivationReason(deactivationReason);
        customer.setDeactivationTimestamp(LocalDateTime.now());
        given(customerRepository.save(any(Customer.class))).willReturn(customer);
        //when
        String res = customerService.deactivateAccount(customerId, deactivationReason);
        //then
        assertNotNull(res);
        then(customerRepository).should(atLeastOnce()).save(any(Customer.class));
    }

    @DisplayName("activate customer test")
    @Test
    void activateAccount() {
        //given
        long customerId = 1L;
        given(customerRepository.findById(customerId)).willReturn(Optional.of(customer));
        customer.setActivationStatus(ActivationStatus.DEACTIVATED);
        given(customerRepository.save(any(Customer.class))).willReturn(customer);
        //when
        String res = customerService.activateAccount(customerId);
        //then
        assertNotNull(res);
        then(customerRepository).should(atLeastOnce()).save(any(Customer.class));
    }
}