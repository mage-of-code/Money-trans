package com.example.Payments.util;

import com.example.Payments.models.Payment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PaymentValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Payment.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Payment payment = (Payment) target;
        if(payment.getPayerAccount()==null){
         errors.rejectValue("receiverAccountId","","Не указан счет отправителя");
        }
        if (payment.getPayerAccount().equals(payment.getReceiverAccount())) {
            errors.rejectValue("receiverAccountId", "", "Счет получателя не может совпадать со счетом отправителя");
        }
        if((payment.getPayerAccount().getBalance().compareTo(payment.getAmount()))<0){
            errors.rejectValue("amount","","Надостаточно средств на счете");
        }
    }


}
