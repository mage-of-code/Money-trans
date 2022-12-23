package com.example.Payments.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class ErrorMessageBuilder {



    public static String buildCorrectErrorMessage(BindingResult bindingResult) {
        StringBuilder errorMsg = new StringBuilder();
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            errorMsg.append(error.getDefaultMessage()).
                    append(";");
        }
        return errorMsg.toString();
    }
}
