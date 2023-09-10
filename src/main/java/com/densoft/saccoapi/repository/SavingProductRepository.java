package com.densoft.saccoapi.repository;

import com.densoft.saccoapi.model.SavingProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SavingProductRepository extends JpaRepository<SavingProduct, Long> {
    Optional<SavingProduct> findByName(String name);

    @Query("SELECT s FROM SavingProduct  s WHERE s.name = :name AND s.id <> :savingProductId")
    Optional<SavingProduct> findByNameExcludeCurrentId(@Param("name") String name, @Param("savingProductId") long savingProductId);
}
