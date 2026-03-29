package com.shop.buildmart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.buildmart.model.Order;
import com.shop.buildmart.model.OrderRequest;
import com.shop.buildmart.service.OrderService;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Order placeOrder(@RequestBody OrderRequest request) {
        return orderService.placeOrder(
                request.getUserId(),
                request.getItems(),
                request.getDistanceKm(),
                request.getPaymentType()
        );
    }
}