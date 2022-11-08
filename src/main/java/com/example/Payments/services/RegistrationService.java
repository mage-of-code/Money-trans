package com.example.Payments.services;

import com.example.Payments.models.Account;
import com.example.Payments.models.User;
import com.example.Payments.models.enums.TypeOfAccount;
import com.example.Payments.repositories.AccountsRepository;
import com.example.Payments.repositories.RoleRepository;
import com.example.Payments.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Collections;

@Service
@Transactional(readOnly = true)
public class RegistrationService {
    private  UsersRepository usersRepository;
    private  AccountService accountService;
    private  AccountsRepository accountsRepository;
    private  PasswordEncoder passwordEncoder;
    private  RoleRepository roleRepository;


    @Autowired
    public RegistrationService(UsersRepository usersRepository, AccountService accountService, AccountsRepository accountsRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.usersRepository = usersRepository;
        this.accountService = accountService;
        this.accountsRepository = accountsRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    @Transactional
    public void register(@Valid User newUser, TypeOfAccount account) {

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRoles(Collections.singleton(roleRepository.findByRoleSignature("ROLE_USER")));
        Account newAccount = accountService.createAccount(newUser, account);
        newUser.setAccounts(Collections.singleton(newAccount));
        usersRepository.save(newUser);
        accountsRepository.save(newAccount);


    }

}
