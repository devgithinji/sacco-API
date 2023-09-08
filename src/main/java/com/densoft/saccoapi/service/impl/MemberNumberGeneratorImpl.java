package com.densoft.saccoapi.service.impl;

import com.densoft.saccoapi.model.Customer;
import com.densoft.saccoapi.repository.CustomerRepository;
import com.densoft.saccoapi.service.MemberNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberNumberGeneratorImpl implements MemberNumberGenerator {

    private final CustomerRepository customerRepository;

    @Override
    public Integer generateAccountNumber() {
        List<Integer> accountNos = customerRepository.findAll().stream().sorted().map(Customer::getMemberNumber).toList();
        if (accountNos.isEmpty()) {
            return 10000000;
        }
        int lastNumber = accountNos.get(accountNos.size() - 1);
        return lastNumber + 1;
    }
}
