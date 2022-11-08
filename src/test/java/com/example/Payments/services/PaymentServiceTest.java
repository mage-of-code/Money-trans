package com.example.Payments.services;

import com.example.Payments.dto.AccountDTO;
import com.example.Payments.dto.PaymentDTO;
import com.example.Payments.models.Account;
import com.example.Payments.models.Payment;
import com.example.Payments.repositories.PaymentsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class PaymentServiceTest {
    @Mock
    private PaymentsRepository paymentsRepository;
    @Mock
    private AccountService accountService;
    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void newTransfer() {

        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        Payment payment = new Payment();
        payment.setPayerAccount(new Account());
        payment.setReceiverAccount(new Account());
        payment.getPayerAccount().setBalance(BigDecimal.valueOf(200));
        payment.getReceiverAccount().setBalance(BigDecimal.valueOf(100));
        payment.setAmount(BigDecimal.valueOf(150));

        paymentService.newTransfer(payment);
        verify(paymentsRepository).save(captor.capture());

        assertEquals(payment, captor.getValue());

        assertEquals(BigDecimal.valueOf(50),payment.getPayerAccount().getBalance());
        assertEquals(BigDecimal.valueOf(250),payment.getReceiverAccount().getBalance());
    }


    @Test
    void findAll() {
        List<Payment> paymentsFromRepo = new ArrayList<>();
        when(paymentsRepository.findAll()).thenReturn(paymentsFromRepo);

        List<Payment> actual = paymentService.findAll();
        assertEquals(paymentsFromRepo,actual);
    }

    @Test
    void findSent() {
        List<Payment> paymentsFromRepo = new ArrayList<>();
        Account account = new Account();
        when(paymentsRepository.findByPayerAccount(account)).thenReturn(paymentsFromRepo);

        List<Payment> actual = paymentService.findSent(account);
        assertEquals(paymentsFromRepo,actual);
    }

    @Test
    void findReceived() {
        List<Payment> paymentsFromRepo = new ArrayList<>();
        Account account = new Account();
        when(paymentsRepository.findByReceiverAccount(account)).thenReturn(paymentsFromRepo);

        List<Payment> actual = paymentService.findReceived(account);
        assertEquals(paymentsFromRepo,actual);
    }
    @Test
    void convertToPaymentDTO(){
        Payment payment=new Payment();
        Account payerAccount = new Account();
        Account receiverAccount = new Account();
        payment.setPayerAccount(payerAccount);
        payment.setReceiverAccount(receiverAccount);
        payment.setAmount(BigDecimal.valueOf(100));
        payment.setComment("comment");

        AccountDTO payerAccountDto = new AccountDTO();
        AccountDTO receiverAccountDto = new AccountDTO();
        when(accountService.convertToDTO(eq(payerAccount))).thenReturn(payerAccountDto);
        when(accountService.convertToDTO(eq(receiverAccount))).thenReturn(receiverAccountDto);

        PaymentDTO expected = new PaymentDTO();
        expected.setAmount(BigDecimal.valueOf(100));
        expected.setComment("comment");
        expected.setReceiverAccount(receiverAccountDto);
        expected.setPayerAccount(payerAccountDto);

        PaymentDTO actual = paymentService.convertToPaymentDTO(payment);

        assertEquals(expected, actual);
    }

    @Test
    void convertToPayment(){
        Account payerAccount = new Account();
        Account receiverAccount = new Account();

        AccountDTO payerAccountDto = new AccountDTO();
        AccountDTO receiverAccountDto = new AccountDTO();
        when(accountService.convertToAccount(eq(payerAccountDto))).thenReturn(payerAccount);
        when(accountService.convertToAccount(eq(receiverAccountDto))).thenReturn(receiverAccount);

        PaymentDTO dto = new PaymentDTO();
        dto.setAmount(BigDecimal.valueOf(100));
        dto.setComment("comment");
        dto.setReceiverAccount(receiverAccountDto);
        dto.setPayerAccount(payerAccountDto);

        Payment expected = new Payment();
        expected.setPayerAccount(payerAccount);
        expected.setReceiverAccount(receiverAccount);
        expected.setAmount(BigDecimal.valueOf(100));
        expected.setComment("comment");

        Payment actual = paymentService.convertToPayment(dto);

        assertEquals(expected,actual);
    }

}