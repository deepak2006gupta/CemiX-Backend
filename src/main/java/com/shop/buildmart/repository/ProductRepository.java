package com.shop.buildmart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.buildmart.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}