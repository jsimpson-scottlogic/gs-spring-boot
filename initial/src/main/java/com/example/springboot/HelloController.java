package com.example.springboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.*;

@RestController
public class HelloController {

	Matcher matcher = new Matcher();

	@GetMapping("/")
	public String index() {
		return "Hello Jessica!";
	}

	@GetMapping("/orders")
	public ArrayList<ArrayList<Order>> orders() {
		ArrayList<ArrayList<Order>> orderData = new ArrayList<ArrayList<Order>>();
		ArrayList<Order> buyList=matcher.buyList;
		orderData.add(buyList);
		ArrayList<Order> sellList=matcher.sellList;
		orderData.add(sellList);
		return orderData;
	}

	@PostMapping("/placeOrder")
	public ArrayList<ArrayList<Order>> placeOrder(@RequestBody Order order) {
		matcher.processOrder(order);
		ArrayList<ArrayList<Order>> orderData = new ArrayList<ArrayList<Order>>();
		ArrayList<Order> buyList=matcher.buyList;
		orderData.add(buyList);
		ArrayList<Order> sellList=matcher.sellList;
		orderData.add(sellList);
	return orderData;
	}
}
