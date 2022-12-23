package com.example.Payments.services;

import com.example.Payments.dto.PaymentDTO;
import com.example.Payments.mapper.PaymentMapper;
import com.example.Payments.models.Account;
import com.example.Payments.models.Payment;
import com.example.Payments.repositories.AccountsRepository;
import com.example.Payments.repositories.PaymentsRepository;
import com.example.Payments.util.Exceptions.AccountNotFoundException;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;


class PaymentServiceTest {
    @Mock
    private PaymentsRepository paymentsRepository;
    @Mock
    private PaymentMapper paymentMapper;
    @Mock
    private AccountsRepository accountsRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
    private PageImpl<Payment> pageFromRepo = new PageImpl<>(getPayments());

    private PageImpl<PaymentDTO> actualDtoPage = new PageImpl<>(getPaymentDTOS());

    private static List<Payment> getPayments() {
        List<Payment> paymentsFromRepo = new ArrayList<>();
        Payment payment = new Payment();
        paymentsFromRepo.add(payment);
        return paymentsFromRepo;
    }

    private static List<PaymentDTO> getPaymentDTOS() {
        PaymentDTO dto = new PaymentDTO();
        List<PaymentDTO> contentInDtoPage = new ArrayList<>();
        contentInDtoPage.add(dto);
        return contentInDtoPage;
    }


    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void newTransferTest() {

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

        assertEquals(BigDecimal.valueOf(50), payment.getPayerAccount().getBalance());
        assertEquals(BigDecimal.valueOf(250), payment.getReceiverAccount().getBalance());


    }


    @Test
    public void findAllTest() {

        when(paymentsRepository.findAll(pageable)).thenReturn(pageFromRepo);
        when(paymentMapper.toDto(pageFromRepo.getContent().get(0))).thenReturn(actualDtoPage.getContent().get(0));

        assertEquals(paymentService.findAll(pageable), actualDtoPage);
    }

    @Test
    public void findAllByAccountIdTest() {

        Account account = new Account();
        when(accountsRepository.findById(1)).thenReturn(Optional.of(account));

        when(paymentsRepository.findByPayerAccount(account, pageable)).thenReturn(pageFromRepo);
        when(paymentsRepository.findByReceiverAccount(account, pageable)).thenReturn(pageFromRepo);
        List<PaymentDTO> contentForActual = getPaymentDTOS();
        contentForActual.addAll(getPaymentDTOS());

        Page<PaymentDTO> actual = new PageImpl<>(contentForActual);

        when(paymentMapper.toDto(pageFromRepo.getContent().get(0))).thenReturn(contentForActual.get(0));

        assertEquals(paymentService.findAllByAccountId(pageable, 1), actual);
    }

    @Test
    public void findAllByAccountId_Trows_AccountNotFound() {
        when(accountsRepository.findById(eq(1))).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> paymentService.findAllByAccountId(pageable, 1));

    }


    @Test
    public void findSentTest() {
        Account account = new Account();
        when(accountsRepository.findById(1)).thenReturn(Optional.of(account));

        when(paymentsRepository.findByPayerAccount(account, pageable)).thenReturn(pageFromRepo);
        when(paymentMapper.toDto(pageFromRepo.getContent().get(0))).thenReturn(actualDtoPage.getContent().get(0));

        assertEquals(paymentService.findSent(pageable, 1), actualDtoPage);
    }

    @Test
    public void findSent_Throws_AccountNotFound() {
        when(accountsRepository.findById(eq(1))).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> paymentService.findSent(pageable, 1));

    }

    @Test
    public void findReceivedTest() {
        Account account = new Account();
        when(accountsRepository.findById(1)).thenReturn(Optional.of(account));

        when(paymentsRepository.findByReceiverAccount(account, pageable)).thenReturn(pageFromRepo);
        when(paymentMapper.toDto(pageFromRepo.getContent().get(0))).thenReturn(actualDtoPage.getContent().get(0));

        assertEquals(paymentService.findReceived(pageable, 1), actualDtoPage);
    }

    @Test
    public void findReceived_Throws_AccountNotFound() {
        when(accountsRepository.findById(eq(1))).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> paymentService.findReceived(pageable, 1));

    }


}