package com.example.Payments.util.Exceptions;

public class PaymentRequestException extends RuntimeException{
    public PaymentRequestException(){super("Отсутствуют права на данный запрос");}
}
