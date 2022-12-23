package com.example.Payments.mapper;

import com.example.Payments.dto.AccountDTO;
import com.example.Payments.dto.PaymentDTO;
import com.example.Payments.models.Account;
import com.example.Payments.models.Payment;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper{
    private ModelMapper modelMapper;

    public AccountMapper(){
        this.modelMapper = new ModelMapper();

        modelMapper.createTypeMap(Account.class, AccountDTO.class)
                .addMappings(account-> account.skip(AccountDTO::setAccountType)).setPostConverter(toDtoConverter());

    }

    private Converter<Account, AccountDTO>toDtoConverter(){
        return context -> {
            Account source = context.getSource();
            AccountDTO destination = context.getDestination();
            destination.setAccountType(source.getAccountType().getName());
            return context.getDestination();
        };
    }

    public AccountDTO toDto(Account entity){
        return  modelMapper.map(entity, AccountDTO.class);
    }

    public Account toEntity(AccountDTO dto){
        return modelMapper.map(dto, Account.class);
    }

}
