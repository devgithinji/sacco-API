package com.densoft.saccoapi.repository;

import com.densoft.saccoapi.model.ActivationStatus;
import com.densoft.saccoapi.model.SavingProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class SavingProductRepositoryTest {

    @Autowired
    private SavingProductRepository savingProductRepository;
    private SavingProduct savingProduct;

    @BeforeEach
    void setUp() {
        savingProduct = new SavingProduct(
                "Eduction Savings",
                "Masomo Savings",
                1.3,
                ActivationStatus.ACTIVE
        );
    }

    @Test
    void findByName() {
        //given
        savingProductRepository.save(savingProduct);
        //when
        Optional<SavingProduct> savingProductOptional = savingProductRepository.findByName(savingProduct.getName());
        //then
        assertTrue(savingProductOptional.isPresent());
        assertEquals(savingProduct.getName(), savingProductOptional.get().getName());
    }

    @Test
    void findByNameExcludeCurrentId() {
        //given
        SavingProduct savingProduct2 = new SavingProduct(
                "Holiday Savings",
                "Holiday Savings",
                1.7,
                ActivationStatus.ACTIVE
        );
        savingProductRepository.saveAll(List.of(savingProduct, savingProduct2));
        //when
        Optional<SavingProduct> optionalSavingProduct = savingProductRepository.findByNameExcludeCurrentId(savingProduct2.getName(), savingProduct.getId());
        //then
        assertTrue(optionalSavingProduct.isPresent());
        assertEquals(savingProduct2.getName(), "Holiday Savings");
    }
}