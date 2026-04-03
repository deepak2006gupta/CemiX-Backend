package com.shop.buildmart.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.buildmart.exception.ResourceNotFoundException;
import com.shop.buildmart.model.Order;
import com.shop.buildmart.model.OrderItem;
import com.shop.buildmart.model.OrderItemRequest;
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

    // ✅ PLACE ORDER
    public Order placeOrder(Long userId, List<OrderItemRequest> items, double distanceKm, String paymentType) {

        double total = 0;

        // Create Order
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus("PLACED");
        order.setPaymentType(paymentType);
        order.setDistanceKm(distanceKm);
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        // Process Items
        for (OrderItemRequest req : items) {

            Product product = productRepository.findById(req.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            // ✅ Stock check
            if (product.getStock() < req.getQuantity()) {
                throw new ResourceNotFoundException("Not enough stock for product: " + product.getName());
            }

            double itemTotal = product.getPrice() * req.getQuantity();
            total += itemTotal;

            // Convert DTO → Entity
            OrderItem item = new OrderItem();
            item.setOrderId(savedOrder.getId());
            item.setProductId(req.getProductId());
            item.setQuantity(req.getQuantity());
            item.setPrice(product.getPrice());

            orderItemRepository.save(item);

            // Reduce stock
            product.setStock(product.getStock() - req.getQuantity());
            productRepository.save(product);
        }

        // Delivery
        double deliveryCharge = distanceKm * 10;

        savedOrder.setDeliveryCharge(deliveryCharge);
        savedOrder.setTotalAmount(total + deliveryCharge);

        return orderRepository.save(savedOrder);
    }

    // ✅ GET ALL ORDERS
    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    // ✅ GET ORDERS BY USER
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }
    public Order updateOrderStatus(Long orderId, String status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setStatus(status);

        return orderRepository.save(order);
    }
}