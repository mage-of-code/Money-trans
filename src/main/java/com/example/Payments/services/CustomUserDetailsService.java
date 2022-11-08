package com.example.Payments.services;

import com.example.Payments.models.User;
import com.example.Payments.repositories.UsersRepository;
import com.example.Payments.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UsersRepository usersRepository;

    @Autowired
    public CustomUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user=usersRepository.findByName(username);
        if (user.isEmpty())
            throw new UsernameNotFoundException("User not found");
        return new UserDetailsImpl(user.get());
    }
}
