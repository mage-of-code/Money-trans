package com.example.Payments.models.AccountBody;

import com.example.Payments.models.enums.TypeOfAccount;

import java.io.Serializable;

public class CreateAccountBody implements Serializable {

    private TypeOfAccount typeOfAccount;

    public TypeOfAccount getTypeOfAccount() {
        return typeOfAccount;
    }

    public void setTypeOfAccount(TypeOfAccount typeOfAccount) {
        this.typeOfAccount = typeOfAccount;
    }
}
