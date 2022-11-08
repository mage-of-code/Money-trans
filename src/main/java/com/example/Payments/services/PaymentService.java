package com.example.Payments.services;

import com.example.Payments.dto.PaymentDTO;
import com.example.Payments.models.Account;
import com.example.Payments.models.Payment;
import com.example.Payments.repositories.AccountsRepository;
import com.example.Payments.repositories.PaymentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PaymentService {

    private PaymentsRepository paymentsRepository;
    private AccountService accountService;

    @Autowired
    public PaymentService(PaymentsRepository paymentsRepository, AccountService accountService) {
        this.paymentsRepository = paymentsRepository;
        this.accountService = accountService;
    }

    @Transactional
    public void newTransfer(Payment payment) {
        Account payer = payment.getPayerAccount();
        Account receiver = payment.getReceiverAccount();
        payer.setBalance(payer.getBalance().subtract(payment.getAmount()));
        receiver.setBalance(receiver.getBalance().add(payment.getAmount()));
        payment.setCreateDate(new Date());
        paymentsRepository.save(payment);

    }

//    public void modifyTransfer(Payment payment) {
//        Account payer = payment.getPayerAccount();
//        Account receiver = payment.getReceiverAccount();
//        payer.setBalance(payer.getBalance().subtract(payment.getAmount()));
//        receiver.setBalance(receiver.getBalance().add(payment.getAmount()));
//        payment.setModifyDate(new Date());
//    }

    public List<Payment> findAll() {
        return new ArrayList<Payment>(paymentsRepository.findAll());
    }

    public List<Payment> findSent(Account account) {
        return new ArrayList<Payment>(paymentsRepository.findByPayerAccount(account));
    }

    public List<Payment> findReceived(Account account) {
        return new ArrayList<Payment>(paymentsRepository.findByReceiverAccount(account));
    }


    public PaymentDTO convertToPaymentDTO(Payment payment) {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setPayerAccount(accountService.convertToDTO(payment.getPayerAccount()));
        paymentDTO.setReceiverAccount(accountService.convertToDTO(payment.getReceiverAccount()));
        paymentDTO.setAmount(payment.getAmount());
        paymentDTO.setComment(payment.getComment());
        return paymentDTO;
    }

    public Payment convertToPayment(PaymentDTO paymentDTO) {
        Payment payment=new Payment();
        payment.setAmount(paymentDTO.getAmount());
        payment.setComment(paymentDTO.getComment());
        payment.setPayerAccount(accountService.convertToAccount(paymentDTO.getPayerAccount()));
        payment.setReceiverAccount(accountService.convertToAccount(paymentDTO.getReceiverAccount()));
        return payment;
    }

}
