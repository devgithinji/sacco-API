package com.densoft.saccoapi.repository;

import com.densoft.saccoapi.model.ActivationStatus;
import com.densoft.saccoapi.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer(
                "John",
                "doe",
                345678,
                "john@gmail.com",
                "0767890456",
                234534534,
                ActivationStatus.ACTIVE);
    }

    @DisplayName("find customer by email or id number or phone number")
    @Test
    void findByEmailOrIdNoOrPhoneNumber() {
        //given
        customerRepository.save(customer);
        //when
        Optional<Customer> customerOptional = customerRepository.findByEmailOrIdNoOrPhoneNumber(customer.getEmail(), customer.getIdNo(), "");
        //then
        assertTrue(customerOptional.isPresent());
        assertEquals(customer.getEmail(), customerOptional.get().getEmail());
    }

    @Test
    void findOtherCustomerWithSimilarDetailsExcludingCurrent() {
        //given
        Customer customer2 = new Customer(
                "Jane",
                "doe",
                354646,
                "jane@gmail.com",
                "0767893454",
                234879797,
                ActivationStatus.ACTIVE);
        customerRepository.saveAll(List.of(customer, customer2));
        //when
        Optional<Customer> customerOptional = customerRepository.findOtherCustomerWithSimilarDetailsExcludingCurrent(
                customer2.getEmail(),
                customer2.getIdNo(),
                "0734534545",
                customer.getId());
        //then
        assertTrue(customerOptional.isPresent());
        assertEquals(customerOptional.get().getEmail(), customer2.getEmail());
    }

    @Test
    void findByMemberNumber() {
        //given
        customerRepository.save(customer);
        //when
        Optional<Customer> customerOptional = customerRepository.findByMemberNumber(customer.getMemberNumber());
        //then
        assertTrue(customerOptional.isPresent());
        assertEquals(customer.getEmail(), customerOptional.get().getEmail());
    }
}