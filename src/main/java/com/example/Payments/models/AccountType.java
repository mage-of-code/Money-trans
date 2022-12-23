package com.example.Payments.models;


import lombok.Data;

import javax.persistence.*;
@Data
@Entity
@Table(name="account_type")
public class AccountType {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    public AccountType(String name){
        this.name = name;
    }

    public AccountType(){};

}
