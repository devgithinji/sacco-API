package com.densoft.saccoapi.service.impl;

import com.densoft.saccoapi.dto.request.CreateTransactionReq;
import com.densoft.saccoapi.dto.response.AllCustomersTotalSavings;
import com.densoft.saccoapi.dto.response.CreateTransactionRes;
import com.densoft.saccoapi.dto.response.CustomerSavingsRes;
import com.densoft.saccoapi.exception.ResourceNotFoundException;
import com.densoft.saccoapi.model.Customer;
import com.densoft.saccoapi.model.PaymentMethod;
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

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;

@Service
@RequiredArgsConstructor
public class SavingsServiceImpl implements SavingsService {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final SavingProductRepository savingProductRepository;
    private final NumberGenerator numberGenerator;

    @Override
    public CreateTransactionRes createTransaction(CreateTransactionReq createTransactionReq) {
        String memberNumber = createTransactionReq.getMemberNumber();
        long savingProductId = createTransactionReq.getSavingProduct();

        Customer customer = customerRepository.findByMemberNumber(Integer.parseInt(memberNumber))
                .orElseThrow(() ->
                        new ResourceNotFoundException("customer", "memberNo", memberNumber));

        SavingProduct savingProduct = savingProductRepository.findById(savingProductId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("savings product", "id", String.valueOf(savingProductId)));

        Transaction transaction = new Transaction(
                numberGenerator.generateTransactionCode(),
                PaymentMethod.valueOf(createTransactionReq.getPaymentMethod().toUpperCase()),
                Double.parseDouble(createTransactionReq.getAmount()),
                customer,
                savingProduct);

        Transaction savedTransaction = transactionRepository.save(transaction);

        String message = "Confirmed: %s credited to: %s account %d for %s".formatted(
                createTransactionReq.getAmount(),
                customer.getFullName(),
                customer.getMemberNumber(),
                savingProduct.getName());

        return new CreateTransactionRes(message, savedTransaction);
    }

    @Override
    public AllCustomersTotalSavings getAllCustomersTotalSavings() {
        Map<String, Double> totalMoneyPerProduct = getTotalMoneyPerSavingsProduct();

        Map<String, Map<Integer, Long>> totalCustomersPerProduct = getTotalCustomersPerSavingsProduct();

        System.out.println(totalCustomersPerProduct);

        Map<String, Object> summary = new HashMap<>();


        totalMoneyPerProduct.forEach((productName, totalMoney) -> {
            Map<String, Object> productSummary = new HashMap<>();
            productSummary.put("total money", totalMoney);
            productSummary.put("total customers", getCustomerNo(totalCustomersPerProduct.get(productName)));
            summary.put(productName, productSummary);
        });

        double totalMoneyForAllProducts = totalMoneyPerProduct.values().stream().mapToDouble(totalMoney -> totalMoney).sum();

        return new AllCustomersTotalSavings(summary, totalMoneyForAllProducts);
    }

    private Integer getCustomerNo(Map<Integer, Long> integerLongMap) {
        return integerLongMap.entrySet().size();
    }

    private Map<String, Double> getTotalMoneyPerSavingsProduct() {
        return transactionRepository.findAll().stream().collect(
                groupingBy(
                        transaction -> transaction.getSavingProduct().getName(),
                        summingDouble(Transaction::getAmount)
                )
        );
    }

    private Map<String, Map<Integer, Long>> getTotalCustomersPerSavingsProduct() {
        return transactionRepository.findAll().stream().collect(
                groupingBy(
                        transaction -> transaction.getSavingProduct().getName(),
                        Collectors.groupingBy(
                                transaction -> transaction.getCustomer().getMemberNumber(),
                                Collectors.counting()
                        )
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
