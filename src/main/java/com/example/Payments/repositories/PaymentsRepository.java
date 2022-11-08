package com.example.Payments.repositories;

import com.example.Payments.models.Account;
import com.example.Payments.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentsRepository extends JpaRepository<Payment,Integer> {
List<Payment>findByPayerAccount(Account payer);
List<Payment>findByReceiverAccount(Account receiver);
}
