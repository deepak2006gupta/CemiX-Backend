package com.shop.buildmart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.buildmart.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
