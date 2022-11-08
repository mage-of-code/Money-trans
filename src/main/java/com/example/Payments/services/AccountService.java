package com.example.Payments.services;

import com.example.Payments.dto.AccountDTO;
import com.example.Payments.models.Account;
import com.example.Payments.models.User;
import com.example.Payments.models.enums.Bic;
import com.example.Payments.models.enums.TypeOfAccount;
import com.example.Payments.repositories.AccountsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class AccountService {

    private AccountsRepository accountsRepository;
    private ModelMapper modelMapper;

    @Autowired
    public AccountService(AccountsRepository accountsRepository, ModelMapper modelMapper) {
        this.accountsRepository = accountsRepository;
        this.modelMapper = modelMapper;
    }

    public Account findByAccountNumber(String number) {
        return accountsRepository.findByAccountNumber(number).orElse(null);
    }

    public Account createAccount(User user, TypeOfAccount accountType) {
        double number = (Math.random() * ((8 - 8) + 1)) + 8;
        Account newAccount = new Account();
        newAccount.setAccountNumber(Double.toString(number));
        newAccount.setBic(Bic.BIC.getBic());
        newAccount.setAccountType(accountType);
        newAccount.setBalance(new BigDecimal(0));
        newAccount.setUser(user);
        return newAccount;
    }

    @Transactional
    public void saveNewAccount(User user, TypeOfAccount account){
        accountsRepository.save(createAccount(user,account));
    }

    public Set<Account> getAccountsByUser(User user){
        return accountsRepository.findByUser(user);
    }

    public AccountDTO convertToDTO(Account account){
        return modelMapper.map(account,AccountDTO.class);
    }
    public Account convertToAccount(AccountDTO dto){
        return modelMapper.map(dto, Account.class);
    }
}
