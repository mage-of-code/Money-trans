package com.example.Payments.services;
import com.example.Payments.dto.PaymentDTO;
import com.example.Payments.mapper.PaymentMapper;
import com.example.Payments.models.Account;
import com.example.Payments.models.Payment;
import com.example.Payments.repositories.AccountsRepository;
import com.example.Payments.repositories.PaymentsRepository;
import com.example.Payments.util.Exceptions.AccountNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class PaymentService {

    private PaymentsRepository paymentsRepository;
    private PaymentMapper paymentMapper;
    private AccountsRepository accountsRepository;

    @Transactional
    public void newTransfer(Payment payment) {
        Account payer = payment.getPayerAccount();
        Account receiver = payment.getReceiverAccount();
        payer.setBalance(payer.getBalance().subtract(payment.getAmount()));
        receiver.setBalance(receiver.getBalance().add(payment.getAmount()));
        payment.setCreateDate(new Date());
        paymentsRepository.save(payment);

    }


    public Page<PaymentDTO> findAll(Pageable pageable) {
        Page<Payment> page = paymentsRepository.findAll(pageable);
        return new PageImpl<>(page.getContent()
                .stream().map(payment -> paymentMapper.toDto(payment))
                .collect(Collectors.toList()));
    }

    public Page<PaymentDTO> findAllByAccountId(Pageable pageable, int accountId) {
        Account account = accountsRepository.findById(accountId).orElse(null);
        if(account ==null){
            throw new AccountNotFoundException();
        }
        List<PaymentDTO> list = new ArrayList<>();
        list.addAll(paymentsRepository.findByPayerAccount(account, pageable)
                .getContent()
                .stream().map(payment -> paymentMapper.toDto(payment)).toList());
        list.addAll(paymentsRepository.findByReceiverAccount(account, pageable)
                .getContent()
                .stream().map(payment -> paymentMapper.toDto(payment)).toList());

        return new PageImpl<>(list);
    }


    public Page<PaymentDTO> findSent(Pageable pageable, int accountId) {
        Account account = accountsRepository.findById(accountId).orElse(null);
        if(account ==null){
            throw new AccountNotFoundException();
        }
        Page<Payment> page = paymentsRepository.findByPayerAccount(account, pageable);

        return new PageImpl<>(page.getContent().stream()
                .map(payment -> paymentMapper.toDto(payment))
                .collect(Collectors.toList()));
    }

    public Page<PaymentDTO> findReceived(Pageable pageable, int accountId) {
        Account account = accountsRepository.findById(accountId).orElse(null);
        if(account ==null){
            throw new AccountNotFoundException();
        }
        Page<Payment> page = paymentsRepository.findByReceiverAccount(account, pageable);

        return new PageImpl<>(page.getContent().stream()
                .map(payment -> paymentMapper.toDto(payment))
                .collect(Collectors.toList()));

    }


}
