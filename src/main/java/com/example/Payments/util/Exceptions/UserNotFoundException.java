package com.example.Payments.util.Exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(){
        super("Пользователь не найден");
    }
    }

