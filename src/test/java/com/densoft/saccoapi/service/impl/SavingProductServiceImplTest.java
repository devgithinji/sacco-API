package com.densoft.saccoapi.service.impl;

import com.densoft.saccoapi.dto.request.CreateSavingProductReq;
import com.densoft.saccoapi.dto.response.CreateSavingProductRes;
import com.densoft.saccoapi.model.ActivationStatus;
import com.densoft.saccoapi.model.SavingProduct;
import com.densoft.saccoapi.repository.SavingProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class SavingProductServiceImplTest {
    @Mock
    private SavingProductRepository savingProductRepository;
    @InjectMocks
    private SavingProductServiceImpl savingProductService;

    private SavingProduct savingProduct;


    @BeforeEach
    void setUp() {
        savingProduct = new SavingProduct(
                1,
                "Eduction Savings",
                "Masomo Savings",
                1.3,
                ActivationStatus.ACTIVE
        );
    }

    @Test
    void createSavingProduct() {
        //given
        CreateSavingProductReq createSavingProductReq = new CreateSavingProductReq(
                "Eduction Savings",
                "Masomo Savings",
                "1.6"
        );
        given(savingProductRepository.save(any(SavingProduct.class))).willReturn(savingProduct);
        given(savingProductRepository.findByName(createSavingProductReq.getName())).willReturn(Optional.empty());
        //when
        CreateSavingProductRes createSavingProductRes = savingProductService.createSavingProduct(createSavingProductReq);
        //then
        assertNotNull(createSavingProductRes);
        assertTrue(createSavingProductRes.getSavingProduct().getId() > 0);
    }

    @Test
    void findSavingProductById() {
        //given
        long productId = 1L;
        given(savingProductRepository.findById(productId)).willReturn(Optional.of(savingProduct));
        //when
        SavingProduct savingProduct = savingProductService.findSavingProductById(productId);
        //then
        assertNotNull(savingProduct);
    }

    @Test
    void getAllSavingProducts() {
        //given
        SavingProduct savingProductTwo = new SavingProduct(
                2,
                "Health Savings",
                "Health Savings",
                1.2,
                ActivationStatus.ACTIVE
        );
        List<SavingProduct> savingProducts = List.of(savingProduct, savingProductTwo);
        given(savingProductRepository.findAll()).willReturn(savingProducts);
        //when
        List<SavingProduct> fetchedSavingProducts = savingProductService.getAllSavingProducts();
        //then
        assertEquals(savingProducts.size(), fetchedSavingProducts.size());
    }

    @Test
    void updateSavingProduct() {
        //given
        long productId = 1L;
        CreateSavingProductReq createSavingProductReq = new CreateSavingProductReq(
                "Eduction Savings",
                "Masomo Savings",
                "1.6"
        );
        given(savingProductRepository.findById(productId)).willReturn(Optional.of(savingProduct));
        given(savingProductRepository.save(any(SavingProduct.class))).willReturn(savingProduct);
        given(savingProductRepository.findByNameExcludeCurrentId(createSavingProductReq.getName(), productId)).willReturn(Optional.empty());
        //when
        CreateSavingProductRes createSavingProductRes = savingProductService.updateSavingProduct(createSavingProductReq, productId);
        //then
        assertNotNull(createSavingProductRes);
        assertEquals(createSavingProductRes.getSavingProduct().getName(), createSavingProductReq.getName());
    }

    @Test
    void activateSavingProduct() {
        //given
        long productId = 1L;
        given(savingProductRepository.save(any(SavingProduct.class))).willReturn(savingProduct);
        savingProduct.setActivationStatus(ActivationStatus.DEACTIVATED);
        given(savingProductRepository.findById(productId)).willReturn(Optional.of(savingProduct));
        //when
        String res = savingProductService.activateSavingProduct(productId);
        //then
        assertNotNull(res);
        then(savingProductRepository).should(atLeastOnce()).save(any(SavingProduct.class));
    }

    @Test
    void deactivateSavingProduct() {
        //given
        long productId = 1L;
        given(savingProductRepository.findById(productId)).willReturn(Optional.of(savingProduct));
        given(savingProductRepository.save(any(SavingProduct.class))).willReturn(savingProduct);
        //when
        String res = savingProductService.deactivateSavingProduct(productId);
        //then
        assertNotNull(res);
        then(savingProductRepository).should(atLeastOnce()).save(any(SavingProduct.class));
    }
}