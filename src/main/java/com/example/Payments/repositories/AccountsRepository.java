package com.example.Payments.repositories;

import com.example.Payments.models.Account;
import com.example.Payments.models.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AccountsRepository extends JpaRepository<Account,Integer> {
    Set<Account> findByUser(User user);

    @Override
    Optional<Account> findById(Integer integer);

    Optional<Account> findByAccountNumber(String number);
}
