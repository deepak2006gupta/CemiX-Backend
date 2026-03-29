package com.shop.buildmart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.buildmart.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhone(String phone);
}