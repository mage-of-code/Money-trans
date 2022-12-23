package com.example.Payments.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class AccountDTO {

    private String accountNumber;
    private String accountType;
    private BigDecimal balance;

}
