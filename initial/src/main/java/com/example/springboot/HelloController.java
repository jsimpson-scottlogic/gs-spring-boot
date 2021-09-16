package com.example.springboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.*;

@RestController
public class HelloController {

	Matcher matcher = new Matcher();

	@GetMapping("/buyOrders")
	public ArrayList<Order> buyOrders() {
		ArrayList<Order> buyList = new ArrayList<Order>();
		buyList=matcher.buyList;
		return buyList;
	}

	@GetMapping("/sellOrders")
	public ArrayList<Order> sellOrders() {
		ArrayList<Order> sellList = new ArrayList<Order>();
		sellList=matcher.sellList;
		return sellList;
	}

	@PostMapping("/placeOrder")
	public ArrayList[] placeOrder(@RequestBody Order order) {
		matcher.processOrder(order);
		ArrayList[] orderData = new ArrayList[2];
		ArrayList<Order> buyList=matcher.buyList;
		orderData[0]=buyList;
		ArrayList<Order> sellList=matcher.sellList;
		orderData[1]=sellList;
	return orderData;
	}
}
