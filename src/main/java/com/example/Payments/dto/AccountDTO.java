package com.example.Payments.dto;

import com.example.Payments.models.enums.TypeOfAccount;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class AccountDTO {
    @NotNull
    private String accountNumber;
    @NotNull
    private TypeOfAccount accountType;

    private BigDecimal balance;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public TypeOfAccount getAccountType() {
        return accountType;
    }

    public void setAccountType(TypeOfAccount accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
