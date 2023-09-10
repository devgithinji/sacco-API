package com.densoft.saccoapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "saving_products")
public class SavingProduct extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(name = "interest_rate", nullable = false)
    private double interestRate;
    @Column(name = "activation_status")
    @Enumerated(EnumType.STRING)
    private ActivationStatus activationStatus;

    public SavingProduct(int id,
                         String name,
                         String description,
                         double interestRate,
                         ActivationStatus activationStatus) {
        super(id);
        this.name = name;
        this.description = description;
        this.interestRate = interestRate;
        this.activationStatus = activationStatus;
    }
}
