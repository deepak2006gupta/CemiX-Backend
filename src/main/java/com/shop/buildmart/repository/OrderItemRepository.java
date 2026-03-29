package com.shop.buildmart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.buildmart.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
