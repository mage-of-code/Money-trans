package com.example.Payments.repositories;

import com.example.Payments.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User,Integer> {
    Optional<User> findByName(String name);
    Optional<User>findById(int id);
    Page<User> findAll(Pageable pageable);


}
