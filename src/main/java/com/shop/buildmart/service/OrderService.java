package com.shop.buildmart.service;

import com.shop.buildmart.model.*;
import com.shop.buildmart.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    public Order placeOrder(Long userId, List<OrderItemRequest> items, double distanceKm, String paymentType) {

        double total = 0;

        for (OrderItemRequest item : items) {
            Product product = productRepository.findById(item.getProductId()).orElseThrow();

            double itemTotal = product.getPrice() * item.getQuantity();
            total += itemTotal;

            item.setPrice(product.getPrice());
        }

        double deliveryCharge = distanceKm * 10; // simple logic

        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(total + deliveryCharge);
        order.setStatus("PLACED");
        order.setPaymentType(paymentType);
        order.setDistanceKm(distanceKm);
        order.setDeliveryCharge(deliveryCharge);
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        for (OrderItemRequest item : items) {
            item.setOrderId(savedOrder.getId());
            orderItemRepository.save(item);
        }

        return savedOrder;
    }
}