package com.densoft.saccoapi.service.impl;

import com.densoft.saccoapi.dto.request.CreateTransactionReq;
import com.densoft.saccoapi.dto.response.AllCustomersTotalSavings;
import com.densoft.saccoapi.dto.response.CreateTransactionRes;
import com.densoft.saccoapi.dto.response.CustomerSavingsRes;
import com.densoft.saccoapi.model.*;
import com.densoft.saccoapi.repository.CustomerRepository;
import com.densoft.saccoapi.repository.SavingProductRepository;
import com.densoft.saccoapi.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class SavingsServiceImplTest {
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private SavingProductRepository savingProductRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private NumberGeneratorImpl numberGenerator;
    @InjectMocks
    private SavingsServiceImpl savingsService;

    private Customer customer;

    private SavingProduct savingProduct;


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

        savingProduct = new SavingProduct(
                1,
                "Eduction Savings",
                "Masomo Savings",
                1.3,
                ActivationStatus.ACTIVE
        );
    }

    @Test
    void createTransaction() {
        //given
        CreateTransactionReq createTransactionReq = new CreateTransactionReq(
                PaymentMethod.CASH.name(),
                "400",
                "234534534",
                2
        );


        given(numberGenerator.generateTransactionCode()).willReturn("234433534546");

        Transaction transaction = new Transaction(
                numberGenerator.generateTransactionCode(),
                PaymentMethod.valueOf(createTransactionReq.getPaymentMethod().toUpperCase()),
                Double.parseDouble(createTransactionReq.getAmount()),
                customer,
                savingProduct);

        given(savingProductRepository.findById(anyLong())).willReturn(Optional.of(savingProduct));
        given(customerRepository.findByMemberNumber(anyInt())).willReturn(Optional.of(customer));
        given(transactionRepository.save(any(Transaction.class))).willReturn(transaction);
        //when
        CreateTransactionRes createTransactionRes = savingsService.createTransaction(createTransactionReq);
        //then
        assertNotNull(createTransactionRes);
    }

    @Test
    void getAllCustomersTotalSavings() {
        //given

        Customer customer2 = new Customer(
                1,
                "Jane",
                "doe",
                34568,
                "jane@gmail.com",
                "0767890987",
                2345389,
                ActivationStatus.ACTIVE);


        SavingProduct savingProduct2 = savingProduct = new SavingProduct(
                2,
                "Holiday Savings",
                "Holiday Savings",
                1.8,
                ActivationStatus.ACTIVE
        );

        Transaction transaction = new Transaction(
                numberGenerator.generateTransactionCode(),
                PaymentMethod.CASH,
                500.0,
                customer,
                savingProduct);

        Transaction transaction2 = new Transaction(
                numberGenerator.generateTransactionCode(),
                PaymentMethod.CASH,
                500.0,
                customer2,
                savingProduct2);

        Transaction transaction3 = new Transaction(
                numberGenerator.generateTransactionCode(),
                PaymentMethod.CASH,
                500.0,
                customer2,
                savingProduct);

        List<Transaction> transactions = List.of(transaction, transaction2, transaction3);
        given(transactionRepository.findAll()).willReturn(transactions);
        //when
        AllCustomersTotalSavings allCustomersTotalSavings = savingsService.getAllCustomersTotalSavings();
        //then
        assertNotNull(allCustomersTotalSavings);
        assertEquals(allCustomersTotalSavings.getTotalSavings(), (transaction.getAmount() + transaction2.getAmount() + transaction3.getAmount()));
    }

    @Test
    void getCustomerSavings() {
        //given
        Customer customer2 = new Customer(
                1,
                "Jane",
                "doe",
                34568,
                "jane@gmail.com",
                "0767890987",
                2345389,
                ActivationStatus.ACTIVE);

        SavingProduct savingProduct2 = savingProduct = new SavingProduct(
                2,
                "Holiday Savings",
                "Holiday Savings",
                1.8,
                ActivationStatus.ACTIVE
        );

        Transaction transaction = new Transaction(
                numberGenerator.generateTransactionCode(),
                PaymentMethod.CASH,
                500.0,
                customer,
                savingProduct);

        Transaction transaction2 = new Transaction(
                numberGenerator.generateTransactionCode(),
                PaymentMethod.CASH,
                500.0,
                customer,
                savingProduct2);

        Transaction transaction3 = new Transaction(
                numberGenerator.generateTransactionCode(),
                PaymentMethod.CASH,
                500.0,
                customer,
                savingProduct);

        Set<Transaction> transactions = Set.of(transaction, transaction2, transaction3);

        customer.setTransactions(transactions);

        given(customerRepository.findByMemberNumber(BDDMockito.anyInt())).willReturn(Optional.of(customer));

        //when
        CustomerSavingsRes customerSavingsRes = savingsService.getCustomerSavings(customer.getMemberNumber());
        //then
        assertNotNull(customerSavingsRes);
        assertEquals(transactions.stream().map(Transaction::getAmount).reduce(0.0, Double::sum), customerSavingsRes.getTotalSavings());
    }

}