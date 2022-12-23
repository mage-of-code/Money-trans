package com.example.Payments.services;

import com.example.Payments.dto.UserDTO;
import com.example.Payments.mapper.UserMapper;
import com.example.Payments.models.Account;
import com.example.Payments.models.AccountType;
import com.example.Payments.models.User;
import com.example.Payments.models.enums.Bic;
import com.example.Payments.repositories.AccountTypeRepository;
import com.example.Payments.repositories.AccountsRepository;
import com.example.Payments.repositories.UsersRepository;
import com.example.Payments.util.Exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

    private UsersRepository usersRepository;
    private AccountsRepository accountsRepository;
    private UserMapper userMapper;
    private AccountTypeRepository accountTypeRepository;

    public Page<UserDTO> findAll(Pageable pageable) {
        Page<User> page = usersRepository.findAll(pageable);
        return new PageImpl<>(page.getContent()
                .stream().map(user -> userMapper.toDto(user))
                .collect(Collectors.toList()));
    }

    public User findOne(int id) {
        User user = usersRepository.findById(id).orElse(null);
        if (user == null) throw new UserNotFoundException();
        return user;
    }


    @Transactional
    public Account createAccount(AccountType accountType) {
        double number = (Math.random() * ((8 - 8) + 1)) + 8;
        Account newAccount = new Account();
        newAccount.setAccountNumber(Double.toString(number));
        newAccount.setBic(Bic.BIC.getBic());
        newAccount.setAccountType(accountTypeRepository.findByName(accountType.getName()).get());
        newAccount.setBalance(new BigDecimal(0));
        accountsRepository.save(newAccount);
        return newAccount;
    }

    public void addAccountToUser(User user, Account account) {
        if (user.getAccounts() == null) {
            user.setAccounts(new HashSet<>());
        }
        user.getAccounts().add(account);
    }


}
