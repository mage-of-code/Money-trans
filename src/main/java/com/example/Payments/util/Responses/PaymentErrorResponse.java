package com.example.Payments.util.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentErrorResponse {
    private String message;
    private long timestamp;

}
