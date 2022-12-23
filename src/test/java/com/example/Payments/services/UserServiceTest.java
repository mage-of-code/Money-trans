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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class UserServiceTest {

    @Mock
    private UsersRepository usersRepository;
    @Mock
    private AccountsRepository accountsRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private AccountTypeRepository accountTypeRepository;
    @InjectMocks
    UserService userService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    public void findAllTest() {
        User user = new User();
        Page<User> pageFromRepo = new PageImpl<>(Collections.singletonList(user));
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
        UserDTO dto = new UserDTO();
        Page<UserDTO> expectedPage = new PageImpl<>(Collections.singletonList(dto));

        when(usersRepository.findAll(pageable)).thenReturn(pageFromRepo);
        when(userMapper.toDto(eq(user))).thenReturn(dto);

        assertEquals(userService.findAll(pageable), expectedPage);


    }

    @Test
    public void findOneTest() {
        User userFromRepo = new User();
        when(usersRepository.findById(eq(1))).thenReturn(Optional.of(userFromRepo));

        User expected = userService.findOne(1);

        assertEquals(userFromRepo, expected);
    }

    @Test
    public void findOneTest_TrowsUserNotFound() {

        when(usersRepository.findById(eq(1))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findOne(1));
    }

    @Test
    public void CreateAccountTest() {
        AccountType accountType = new AccountType();
        when(accountTypeRepository.findByName(eq(accountType.getName()))).thenReturn(Optional.of(accountType));

        Account expected = new Account();
        expected.setBic(Bic.BIC.getBic());
        expected.setAccountType(accountType);
        expected.setBalance(BigDecimal.valueOf(0));
        expected.setAccountNumber("number");

        Account actual = userService.createAccount(accountType);

        verify(accountsRepository).save(actual);

        assertEquals(expected.getBic(), actual.getBic());
        assertEquals(expected.getAccountType(), actual.getAccountType());
        assertEquals(expected.getBalance(), actual.getBalance());
        assertInstanceOf(String.class, actual.getAccountNumber());
    }

}