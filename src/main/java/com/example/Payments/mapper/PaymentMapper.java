package com.example.Payments.mapper;

import com.example.Payments.dto.AccountDTO;
import com.example.Payments.dto.PaymentDTO;
import com.example.Payments.dto.UserDTO;
import com.example.Payments.models.Account;
import com.example.Payments.models.Payment;
import com.example.Payments.models.User;
import com.example.Payments.repositories.AccountsRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component
public class PaymentMapper {
    private ModelMapper modelMapper;
    private AccountsRepository accountsRepository;

    @Autowired
    public PaymentMapper(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
        this.modelMapper = new ModelMapper();

        modelMapper.createTypeMap(Payment.class, PaymentDTO.class)
                .addMappings(payment -> payment.skip(PaymentDTO::setAccountNumberOfPayer))
                .addMappings(payment -> payment.skip(PaymentDTO::setAccountNumberOfReceiver))
                .setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(PaymentDTO.class, Payment.class)
                .addMappings(payment -> payment.skip(Payment::setPayerAccount))
                .addMappings(payment -> payment.skip(Payment::setReceiverAccount))
                .setPostConverter(toEntityConverter());
    }

    private Converter<Payment, PaymentDTO> toDtoConverter() {
        return context -> {
            Payment source = context.getSource();
            PaymentDTO destination = context.getDestination();
            destination.setAccountNumberOfPayer(source.getPayerAccount().getAccountNumber());
            destination.setAccountNumberOfReceiver(source.getReceiverAccount().getAccountNumber());
            return context.getDestination();
        };
    }

    private Converter<PaymentDTO, Payment> toEntityConverter() {
        return context -> {
            PaymentDTO source = context.getSource();
            Payment destination = context.getDestination();
            destination.setPayerAccount(accountsRepository.findByAccountNumber(source.getAccountNumberOfPayer()).get());
            destination.setReceiverAccount(accountsRepository.findByAccountNumber(source.getAccountNumberOfReceiver()).get());
            return context.getDestination();
        };
    }


    public PaymentDTO toDto(Payment entity) {
        return modelMapper.map(entity, PaymentDTO.class);
    }

    public Payment toEntity(PaymentDTO dto) {
        return modelMapper.map(dto, Payment.class);
    }
}
