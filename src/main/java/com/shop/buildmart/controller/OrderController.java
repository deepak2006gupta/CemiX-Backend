package com.shop.buildmart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.shop.buildmart.model.Order;
import com.shop.buildmart.model.OrderRequest;
import com.shop.buildmart.service.OrderService;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    // ✅ PLACE ORDER
    @PostMapping
    public Order placeOrder(@Valid @RequestBody OrderRequest request) {
        return orderService.placeOrder(
                request.getUserId(),
                request.getItems(),
                request.getDistanceKm(),
                request.getPaymentType()
        );
    }

    // ✅ GET ALL ORDERS
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // ✅ GET ORDERS BY USER
    @GetMapping("/user/{userId}")
    public List<Order> getUserOrders(@PathVariable Long userId) {
        return orderService.getUserOrders(userId);
    }

    // ✅ UPDATE ORDER STATUS
    @PutMapping("/{id}/status")
    public Order updateStatus(@PathVariable Long id,
                            @RequestParam String status) {
        return orderService.updateOrderStatus(id, status);
    }
}