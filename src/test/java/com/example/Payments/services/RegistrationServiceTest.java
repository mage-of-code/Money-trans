package com.example.Payments.services;

import com.example.Payments.dto.LoginInfo;
import com.example.Payments.models.Account;
import com.example.Payments.models.AccountType;
import com.example.Payments.models.Role;
import com.example.Payments.models.User;
import com.example.Payments.repositories.AccountTypeRepository;
import com.example.Payments.repositories.RoleRepository;
import com.example.Payments.repositories.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class RegistrationServiceTest {
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private AccountTypeRepository accountTypeRepository;

    @InjectMocks
    RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void register() {

        LoginInfo loginInfo = new LoginInfo();
        User user = new User();
        user.setPassword("password");
        Role role = new Role();


        when(modelMapper.map(loginInfo, User.class)).thenReturn(user);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(roleRepository.findByRoleSignature("ROLE_USER")).thenReturn(role);

        AccountType accountType = new AccountType("checking");
        Account account = new Account();

        when(accountTypeRepository.findByName(eq("checking"))).thenReturn(Optional.of(accountType));
        when(userService.createAccount(accountType)).thenReturn(account);

        User expectedUser = new User();
        expectedUser.setPassword("encodedPassword");
        expectedUser.setRoles(Collections.singleton(role));
        account.setAccountType(accountType);
        expectedUser.setAccounts(Collections.singleton(account));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        registrationService.register(loginInfo);


        verify(userService).addAccountToUser(captor.capture(), eq(account));
        assertEquals(expectedUser.getPassword(), captor.getValue().getPassword());
        assertTrue(expectedUser.getRoles().containsAll(captor.getValue().getRoles()));

        verify(usersRepository).save(user);

    }

}