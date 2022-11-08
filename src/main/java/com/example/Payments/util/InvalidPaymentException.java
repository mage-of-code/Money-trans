package com.example.Payments.util;

public class InvalidPaymentException extends RuntimeException{
    public InvalidPaymentException(String msg){
        super(msg);
    }
}
