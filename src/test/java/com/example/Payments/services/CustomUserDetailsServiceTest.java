package com.example.Payments.services;

import com.example.Payments.models.User;
import com.example.Payments.repositories.UsersRepository;
import com.example.Payments.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class CustomUserDetailsServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void loadUserByUsername() {

        User user = new User();
        user.setName("Name");
        UserDetailsImpl expected = new UserDetailsImpl(user);

        when(usersRepository.findByName(eq("Name"))).thenReturn(Optional.of(user));

        UserDetails actual = customUserDetailsService.loadUserByUsername("Name");

        assertEquals(expected.getUser().getName(),actual.getUsername());

    }
}