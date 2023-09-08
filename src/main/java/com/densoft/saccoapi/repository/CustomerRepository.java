package com.densoft.saccoapi.repository;

import com.densoft.saccoapi.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {


    Optional<Customer> findByEmailOrIdNoOrPhoneNumber(String email, int idNo, String phoneNumber);

    @Query("SELECT c FROM Customer  c WHERE (c.email = :email OR c.idNo = :idNo OR c.phoneNumber = :phoneNumber) AND c.id <> :customerId")
    Optional<Customer> findOtherCustomerWithSimilarDetailsExcludingCurrent(@Param("email") String email,
                                                                           @Param("idNo") int idNo,
                                                                           @Param("phoneNumber") String phoneNumber,
                                                                           @Param("customerId") long customerId);

}
