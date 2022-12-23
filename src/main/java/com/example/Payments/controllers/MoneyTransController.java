package com.example.Payments.controllers;

import com.example.Payments.dto.PaymentDTO;
import com.example.Payments.mapper.PaymentMapper;
import com.example.Payments.models.Payment;
import com.example.Payments.services.PaymentService;
import com.example.Payments.util.ErrorMessageBuilder;
import com.example.Payments.util.Exceptions.InvalidPaymentException;
import com.example.Payments.util.Responses.PaymentErrorResponse;
import com.example.Payments.util.validations.PaymentValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/money-trans")
@AllArgsConstructor
public class MoneyTransController {

    private PaymentService paymentService;
    private PaymentValidator paymentValidator;
    private PaymentMapper paymentMapper;

    @PostMapping("/new-transfer")
    public ResponseEntity<String> transfer(@RequestBody @Valid PaymentDTO paymentDTO, BindingResult bindingResult) {
        Payment payment = paymentMapper.toEntity(paymentDTO);
        paymentValidator.validate(payment, bindingResult);
        if (bindingResult.hasErrors())
            throw new InvalidPaymentException(ErrorMessageBuilder.buildCorrectErrorMessage(bindingResult));

        paymentService.newTransfer(payment);
        return ResponseEntity.ok("Перевод выполнен");

    }

    @ExceptionHandler
    public ResponseEntity<PaymentErrorResponse> handleException(InvalidPaymentException e) {
        PaymentErrorResponse response = new PaymentErrorResponse(e.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }



}
