package com.densoft.saccoapi.service.impl;

import com.densoft.saccoapi.dto.request.CreateTransactionReq;
import com.densoft.saccoapi.dto.response.AllCustomersTotalSavings;
import com.densoft.saccoapi.dto.response.CreateTransactionRes;
import com.densoft.saccoapi.dto.response.CustomerSavingsRes;
import com.densoft.saccoapi.exception.ResourceNotFoundException;
import com.densoft.saccoapi.model.Customer;
import com.densoft.saccoapi.model.SavingProduct;
import com.densoft.saccoapi.model.Transaction;
import com.densoft.saccoapi.repository.CustomerRepository;
import com.densoft.saccoapi.repository.SavingProductRepository;
import com.densoft.saccoapi.repository.TransactionRepository;
import com.densoft.saccoapi.service.NumberGenerator;
import com.densoft.saccoapi.service.SavingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class SavingsServiceImpl implements SavingsService {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final SavingProductRepository savingProductRepository;
    private final NumberGenerator numberGenerator;

    @Override
    public CreateTransactionRes createTransaction(CreateTransactionReq createTransactionReq) {
        long customerId = createTransactionReq.getCustomer();
        long savingProductId = createTransactionReq.getSavingProduct();

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("customer", "id", String.valueOf(customerId)));

        SavingProduct savingProduct = savingProductRepository.findById(savingProductId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("savings product", "id", String.valueOf(savingProductId)));

        Transaction transaction = new Transaction(
                numberGenerator.generateTransactionCode(),
                createTransactionReq.getPaymentMethod(),
                createTransactionReq.getAmount(),
                customer,
                savingProduct);

        Transaction savedTransaction = transactionRepository.save(transaction);

        String message = "Confirmed: %s credited to: %s account%d for %s".formatted(createTransactionReq.getAmount(), customer.getFullName(), customer.getMemberNumber(), savingProduct.getName());

        return new CreateTransactionRes(message, savedTransaction);
    }

    @Override
    public AllCustomersTotalSavings getAllCustomersTotalSavings() {
        Map<String, Double> totalMoneyPerProduct = getTotalMoneyPerSavingsProduct();

        Map<String, Long> totalCustomersPerProduct = getTotalCustomersPerSavingsProduct();

        Map<String, Object> summary = new HashMap<>();

        totalMoneyPerProduct.forEach((productName, totalMoney) -> {
            Map<String, Object> productSummary = new HashMap<>();
            productSummary.put("total money", totalMoney);
            productSummary.put("total customers", totalCustomersPerProduct.getOrDefault(productName, 0L));
            summary.put(productName, productSummary);
        });


        double totalMoneyForAllProducts = totalMoneyPerProduct.values().stream().mapToDouble(totalMoney -> totalMoney).sum();


        return new AllCustomersTotalSavings(summary, totalMoneyForAllProducts);
    }

    private Map<String, Double> getTotalMoneyPerSavingsProduct() {
        return transactionRepository.findAll().stream().collect(
                groupingBy(
                        transaction -> transaction.getSavingProduct().getName(),
                        summingDouble(Transaction::getAmount)
                )
        );
    }

    private Map<String, Long> getTotalCustomersPerSavingsProduct() {
        return transactionRepository.findAll().stream().collect(
                groupingBy(
                        transaction -> transaction.getSavingProduct().getName(),
                        counting()
                )
        );
    }

    @Override
    public CustomerSavingsRes getCustomerSavings(int memberNumber) {
        Customer customer = customerRepository.findByMemberNumber(memberNumber)
                .orElseThrow(() -> new ResourceNotFoundException("customer", "account number", String.valueOf(memberNumber)));

        Map<String, Double> summaryPerSavingProduct = customer.getTransactions().stream().collect(Collectors.groupingBy(
                transaction -> transaction.getSavingProduct().getName(),
                Collectors.summingDouble(Transaction::getAmount)
        ));

        double totalSavingsForALLSavingsProducts = summaryPerSavingProduct.values().stream().mapToDouble(value -> value).sum();

        return new CustomerSavingsRes(customer, summaryPerSavingProduct, totalSavingsForALLSavingsProducts);
    }
}
