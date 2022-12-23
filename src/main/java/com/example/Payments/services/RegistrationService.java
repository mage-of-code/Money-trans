package com.example.Payments.services;

import com.example.Payments.dto.LoginInfo;

import com.example.Payments.models.User;
import com.example.Payments.repositories.AccountTypeRepository;
import com.example.Payments.repositories.RoleRepository;
import com.example.Payments.repositories.UsersRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Collections;

@AllArgsConstructor
@Service
public class RegistrationService {
    private UsersRepository usersRepository;
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private ModelMapper modelMapper;

    private AccountTypeRepository accountTypeRepository;


    @Transactional
    public void register(@Valid LoginInfo loginInfo) {
        User newUser = modelMapper.map(loginInfo, User.class);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRoles(Collections.singleton(roleRepository.findByRoleSignature("ROLE_USER")));
        userService.addAccountToUser(newUser, userService.createAccount(accountTypeRepository.findByName("checking").get()));


        usersRepository.save(newUser);
    }

}
