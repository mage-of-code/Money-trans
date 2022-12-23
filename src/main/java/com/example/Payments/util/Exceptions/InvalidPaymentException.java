package com.example.Payments.util.Exceptions;

public class InvalidPaymentException extends RuntimeException{
    public InvalidPaymentException(String msg){
        super(msg);
    }
}
