package com.example.service;

import com.example.repository.OrderRepository;
import com.example.springboot.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public void save(Orders order)
    {
        orderRepository.save(order);
    }
}
