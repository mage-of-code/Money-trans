package com.example.Payments.services;

import com.example.Payments.models.Account;
import com.example.Payments.models.Role;
import com.example.Payments.models.User;
import com.example.Payments.models.enums.TypeOfAccount;
import com.example.Payments.repositories.AccountsRepository;
import com.example.Payments.repositories.RoleRepository;
import com.example.Payments.repositories.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class RegistrationServiceTest {
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private  AccountService accountService;
    @Mock
    private AccountsRepository accountsRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void register(){
       User newUser = new User();

        TypeOfAccount typeOfAccount = TypeOfAccount.INDIVIDUAL;
        Account account = new Account();
        account.setUser(newUser);
        account.setAccountType(typeOfAccount);

        Role role = new Role();
        role.setRoleSignature("ROLE_USER");
        newUser.setPassword("password");
        newUser.setRoles(Collections.singleton(role));
        newUser.setAccounts(Collections.singleton(account));

        when(passwordEncoder.encode(eq("password"))).thenReturn("password");
        when(roleRepository.findByRoleSignature(eq("ROLE_USER"))).thenReturn(role);
        when(accountService.createAccount(eq(newUser),eq(typeOfAccount))).thenReturn(account);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        registrationService.register(newUser,typeOfAccount);
        verify(usersRepository).save(captor.capture());

        assertEquals(newUser, captor.getValue());

        ArgumentCaptor<Account> captor2= ArgumentCaptor.forClass(Account.class);
        verify(accountsRepository).save(captor2.capture());

        assertEquals(account,captor2.getValue());

    }

}