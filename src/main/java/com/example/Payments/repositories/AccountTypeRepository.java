package com.example.Payments.repositories;

import com.example.Payments.models.AccountType;
import net.bytebuddy.description.type.TypeDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType,String> {
    Optional<AccountType> findByName(String name);
}
