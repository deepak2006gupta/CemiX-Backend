package com.shop.buildmart.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.buildmart.model.Order;
import com.shop.buildmart.model.OrderItem;
import com.shop.buildmart.model.Product;
import com.shop.buildmart.repository.OrderItemRepository;
import com.shop.buildmart.repository.OrderRepository;
import com.shop.buildmart.repository.ProductRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    public Order placeOrder(Long userId, List<OrderItem> items, double distanceKm, String paymentType) {

        double total = 0;

        // Step 1: Create Order first
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus("PLACED");
        order.setPaymentType(paymentType);
        order.setDistanceKm(distanceKm);
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        // Step 2: Process each item
        for (OrderItem req : items) {

            Product product = productRepository.findById(req.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // Calculate total
            double itemTotal = product.getPrice() * req.getQuantity();
            total += itemTotal;

            // Step 3: Create OrderItem (ENTITY)
            OrderItem item = new OrderItem();
            item.setOrderId(savedOrder.getId());
            item.setProductId(req.getProductId());
            item.setQuantity(req.getQuantity());
            item.setPrice(product.getPrice());

            orderItemRepository.save(item);

            // Step 4: Reduce stock
            product.setStock(product.getStock() - req.getQuantity());
            productRepository.save(product);
        }

        // Step 5: Add delivery charge
        double deliveryCharge = distanceKm * 10;

        savedOrder.setDeliveryCharge(deliveryCharge);
        savedOrder.setTotalAmount(total + deliveryCharge);

        // Step 6: Save updated order
        return orderRepository.save(savedOrder);
    }
}