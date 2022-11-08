package com.example.Payments.services;

import com.example.Payments.dto.AccountDTO;
import com.example.Payments.dto.UserInfo;
import com.example.Payments.models.Account;
import com.example.Payments.models.Payment;
import com.example.Payments.models.User;
import com.example.Payments.repositories.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class UserServiceTest {
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private AccountService accountService;
    @InjectMocks
    UserService userService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void findAll() {
        List<User> usersFromRepo = new ArrayList<>();
        when(usersRepository.findAll()).thenReturn(usersFromRepo);

        List<User> actual = userService.findAll();
        assertEquals(usersFromRepo, actual);
    }

    @Test
    void findOne() {
        User userFromRepo = new User();
        int id = 1;
        when(usersRepository.findById(eq(id))).thenReturn(Optional.of(userFromRepo));

        User actual = userService.findOne(id);

        assertEquals(userFromRepo, actual);
    }

    @Test
    void convertToUserInfo() {
        User user = new User();
        AccountDTO accountDTO = new AccountDTO();
        Account account = new Account();
        user.setName("Name");
        user.setAccounts(Collections.singleton(account));

        when(accountService.convertToDTO(eq(account))).thenReturn(accountDTO);

        UserInfo expected=new UserInfo();
        expected.setName("Name");
        expected.setAccountDTOSet(Collections.singleton(accountDTO));

        UserInfo actual = userService.convertToUserInfo(user);
        assertEquals(expected, actual);

    }
}