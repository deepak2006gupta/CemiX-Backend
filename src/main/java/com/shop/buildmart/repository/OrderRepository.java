package com.shop.buildmart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.buildmart.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByOrderByCreatedAtDesc();

    public List<Order> findByUserId(Long userId);

}
