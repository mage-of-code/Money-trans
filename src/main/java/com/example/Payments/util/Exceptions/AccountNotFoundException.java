package com.example.Payments.util.Exceptions;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(){
        super("Запрашиваемый счет не найден");
    }
}
