package com.example.Payments.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class PaymentDTO {

    @NotEmpty(message = "Введите номер счета отправителя")
    private String accountNumberOfPayer;

    @NotEmpty(message = "Введите номер счета получателя")
    private String accountNumberOfReceiver;

    @Min(value = 0, message = "Суммма перевода должна быть больше 0")
    private BigDecimal amount;

    @Size(min = 0, max = 150, message = "Максимальный размер комментария - 150 символов")
    private String comment;
}

