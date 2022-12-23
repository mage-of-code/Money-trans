package com.example.Payments.util.validations;


import com.example.Payments.models.User;
import com.example.Payments.util.Exceptions.PaymentRequestException;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;


@Component
public class PaymentRequestValidator  {
    public void validate(User user, Integer accountId) {
        Set<Integer> accountsId = user.getAccounts().stream().map(account -> account.getId()).collect(Collectors.toSet());
        if (!accountsId.contains(accountId))
            throw new PaymentRequestException();
    }
}
