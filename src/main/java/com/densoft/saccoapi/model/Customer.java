package com.densoft.saccoapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "customers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends BaseEntity {
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "id_no", nullable = false)
    private int idNo;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String phoneNumber;
    @Column(name = "member_number", nullable = false, unique = true)
    private int memberNumber;
    @Enumerated(EnumType.STRING)
    private ActivationStatus activationStatus;
    @Column(name = "deactivation_reason")
    private String deactivationReason;
    @Column(name = "deactivation_timestamp")
    protected LocalDateTime deactivationTimestamp;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Transaction> transactions = new LinkedHashSet<>();
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "customer_savings_products",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "savings_product_id")
    )
    private Set<SavingProduct> savingProducts = new LinkedHashSet<>();


    public Customer(String firstName,
                    String lastName,
                    int idNo,
                    String email,
                    String phoneNumber,
                    int memberNumber,
                    ActivationStatus activationStatus) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.idNo = idNo;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.memberNumber = memberNumber;
        this.activationStatus = activationStatus;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}
