package com.example.Payments.services;

import com.example.Payments.models.Account;
import com.example.Payments.models.User;
import com.example.Payments.models.enums.Bic;
import com.example.Payments.models.enums.TypeOfAccount;
import com.example.Payments.repositories.AccountsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.MockitoAnnotations.openMocks;

class AccountServiceTest {

    @Mock
    private AccountsRepository accountsRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void findByAccountNumber() {

        Account accountFromRepo = new Account();
        when(accountsRepository.findByAccountNumber(eq("adf"))).thenReturn(Optional.of(accountFromRepo));

        Account actual = accountService.findByAccountNumber("adf");
        assertEquals(accountFromRepo, actual);
    }

    @Test
    void createAccount() {

        User user = new User();
        TypeOfAccount accountType = TypeOfAccount.INDIVIDUAL;

        Account actual = accountService.createAccount(user, accountType);

        assertNotNull(actual.getAccountNumber());
        assertEquals("123456789", actual.getBic());
        assertEquals(accountType, actual.getAccountType());
        assertEquals(BigDecimal.ZERO, actual.getBalance());
        assertEquals(user, actual.getUser());
    }

    @Test
    void saveNewAccount(){
    User user = new User();
    TypeOfAccount typeOfAccount = TypeOfAccount.INDIVIDUAL;
    Account account = new Account();

    AccountService spy = spy(accountService);
    when(spy.createAccount(eq(user),eq(typeOfAccount))).thenReturn(account);
    spy.saveNewAccount(user,typeOfAccount);
    verify(accountsRepository).save(eq(account));
    }

    @Test
    void getAccountsByUser(){
        User user = new User();
        Set<Account> setFromRepo = new HashSet<>();
        when(accountsRepository.findByUser(eq(user))).thenReturn(setFromRepo);

        Set<Account> actual = accountService.getAccountsByUser(user);
        assertEquals(setFromRepo,actual);
    }


}