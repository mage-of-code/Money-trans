package com.example.Payments.repositories;

import com.example.Payments.models.Account;
import com.example.Payments.models.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface PaymentsRepository extends JpaRepository<Payment,Integer> {
Page<Payment> findByPayerAccount(Account account, Pageable pageable);
Page<Payment> findByReceiverAccount(Account account, Pageable pageable);
Page<Payment> findAll(Pageable pageable);
}
