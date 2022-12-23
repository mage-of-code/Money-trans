package com.example.Payments.models;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "bic")
    private String bic;

    @ManyToOne
    @JoinColumn(name = "account_type", referencedColumnName = "id")
    private AccountType accountType;

    @Column(name = "balance")
    private BigDecimal balance;

    @OneToMany(mappedBy = "payerAccount", fetch = FetchType.EAGER)
    private List<Payment> sentPayments;

    @OneToMany(mappedBy = "receiverAccount", fetch = FetchType.EAGER)
    private List<Payment> receivedPayments;

}
