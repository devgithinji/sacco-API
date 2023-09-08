package com.densoft.saccoapi.service.impl;

import com.densoft.saccoapi.model.Customer;
import com.densoft.saccoapi.repository.CustomerRepository;
import com.densoft.saccoapi.service.NumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class NumberGeneratorImpl implements NumberGenerator {

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

    @Override
    public String generateTransactionCode() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = dateFormat.format(new Date());
        Random random = new Random();
        int randomValue = random.nextInt(10000);

        return "SACCO" + timestamp + String.format("%04d", randomValue);
    }
}
