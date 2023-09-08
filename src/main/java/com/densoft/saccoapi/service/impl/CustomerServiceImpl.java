package com.densoft.saccoapi.service.impl;

import com.densoft.saccoapi.dto.request.CreateCustomerReq;
import com.densoft.saccoapi.dto.response.CreateCustomerRes;
import com.densoft.saccoapi.exception.APIException;
import com.densoft.saccoapi.exception.ResourceNotFoundException;
import com.densoft.saccoapi.model.ActivationStatus;
import com.densoft.saccoapi.model.Customer;
import com.densoft.saccoapi.repository.CustomerRepository;
import com.densoft.saccoapi.service.CustomerService;
import com.densoft.saccoapi.service.MemberNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final MemberNumberGenerator memberNumberGenerator;


    @Override
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomerById(long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("customer", "id", String.valueOf(customerId)));
    }

    @Override
    public CreateCustomerRes saveCustomer(CreateCustomerReq createCustomerReq) {
        Optional<Customer> customerOptional = customerRepository.findByEmailOrIdNoOrPhoneNumber(
                createCustomerReq.getEmail(),
                createCustomerReq.getIdNo(),
                createCustomerReq.getPhoneNumber()
        );

        if (customerOptional.isPresent()) throw new APIException("existing customer found with similar details");

        Customer customer = new Customer(
                createCustomerReq.getFirstName(),
                createCustomerReq.getLastName(),
                createCustomerReq.getIdNo(),
                createCustomerReq.getEmail(),
                createCustomerReq.getPhoneNumber(),
                memberNumberGenerator.generateAccountNumber(),
                ActivationStatus.ACTIVE);

        Customer savedCustomer = customerRepository.save(customer);

        return new CreateCustomerRes("Customer created successfully", savedCustomer);
    }

    @Override
    public CreateCustomerRes updateCustomer(long customerId, CreateCustomerReq createCustomerReq) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isEmpty())
            throw new ResourceNotFoundException("customer", "id", String.valueOf(customerId));

        Optional<Customer> collidingCustomers = customerRepository.findOtherCustomerWithSimilarDetailsExcludingCurrent(
                createCustomerReq.getEmail(),
                createCustomerReq.getIdNo(),
                createCustomerReq.getPhoneNumber(),
                customerId
        );

        if (collidingCustomers.isPresent()) throw new APIException("Other customers found with similar details");

        Customer existingCustomer = customerOptional.get();
        existingCustomer.setFirstName(createCustomerReq.getFirstName());
        existingCustomer.setLastName(createCustomerReq.getLastName());
        existingCustomer.setEmail(createCustomerReq.getEmail());
        existingCustomer.setPhoneNumber(createCustomerReq.getPhoneNumber());
        existingCustomer.setIdNo(createCustomerReq.getIdNo());

        Customer updatedCustomer = customerRepository.save(existingCustomer);

        return new CreateCustomerRes("customer updated successfully", updatedCustomer);
    }

    @Override
    public String deactivateAccount(long customerId, String deactivationReason) {
        return toggleAccountStatus(customerId, ActivationStatus.DEACTIVATED, deactivationReason);
    }


    @Override
    public String activateAccount(long customerId) {
        return toggleAccountStatus(customerId, ActivationStatus.ACTIVE, "");
    }


    private String toggleAccountStatus(long customerId, ActivationStatus activationStatus, String reason) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isEmpty())
            throw new ResourceNotFoundException("customer", "id", String.valueOf(customerId));

        Customer existingCustomer = customerOptional.get();
        existingCustomer.setActivationStatus(activationStatus);

        if (activationStatus.equals(ActivationStatus.DEACTIVATED)) {
            existingCustomer.setDeactivationReason(reason);
            existingCustomer.setDeactivationTimestamp(LocalDateTime.now());
        } else {
            existingCustomer.setDeactivationReason("");
            existingCustomer.setDeactivationTimestamp(null);
        }

        customerRepository.save(existingCustomer);

        String action = activationStatus.equals(ActivationStatus.DEACTIVATED) ? "deactivated" : "activated";

        return "customer number: %d %s".formatted(existingCustomer.getMemberNumber(), action);
    }
}
