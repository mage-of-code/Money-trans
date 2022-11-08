package com.example.Payments.controllers;

import com.example.Payments.dto.PaymentDTO;
import com.example.Payments.models.Payment;
import com.example.Payments.repositories.AccountsRepository;
import com.example.Payments.services.PaymentService;

import com.example.Payments.util.InvalidPaymentException;
import com.example.Payments.util.PaymentErrorResponse;
import com.example.Payments.util.PaymentValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/money-trans")
public class MoneyTransController {

    private PaymentService paymentService;
    private ModelMapper modelMapper;
    private PaymentValidator paymentValidator;
    private AccountsRepository accountsRepository;

    @Autowired
    public MoneyTransController(PaymentService paymentService, ModelMapper modelMapper, PaymentValidator paymentValidator, AccountsRepository accountsRepository) {
        this.paymentService = paymentService;

        this.modelMapper = modelMapper;

        this.paymentValidator = paymentValidator;
        this.accountsRepository = accountsRepository;
    }


    @PostMapping("/transfer-money")
    public ResponseEntity<String> transfer(@RequestBody @Valid PaymentDTO paymentDTO, BindingResult bindingResult) {
     Payment payment=paymentService.convertToPayment(paymentDTO);
        paymentValidator.validate(payment, bindingResult);
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField()).append(" - ").
                        append(error.getDefaultMessage()).
                        append(";");
            }
            throw new InvalidPaymentException(errorMsg.toString());
        }
        paymentService.newTransfer(payment);
        return ResponseEntity.ok("Перевод выполнен");

    }

    @GetMapping
    public List<PaymentDTO> getPayments() {
        return paymentService.findAll().stream().map(payment->paymentService.convertToPaymentDTO(payment)).collect(Collectors.toList());
    }

    @GetMapping("/my-transfers/{accountId}/sent")
    public List<PaymentDTO> getSentPayments(@PathVariable("accountId") Integer accountId) {
        return paymentService.findSent(accountsRepository.findById(accountId).orElse(null))
                .stream().map(payment->paymentService.convertToPaymentDTO(payment)).collect(Collectors.toList());
    }

    @GetMapping("/my-transfers/{accountId}/received")
    public List<PaymentDTO> getReceivedPayments(@PathVariable("accountId") Integer accountId) {
        return paymentService.findReceived(accountsRepository.findById(accountId).orElse(null))
                .stream().map(payment->paymentService.convertToPaymentDTO(payment)).collect(Collectors.toList());
    }


    @ExceptionHandler
    public ResponseEntity<PaymentErrorResponse> handleException(InvalidPaymentException e) {
        PaymentErrorResponse response = new PaymentErrorResponse(e.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
