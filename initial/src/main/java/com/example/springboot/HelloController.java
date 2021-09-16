package com.example.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.*;

@RestController
public class HelloController {

	@Autowired
	Matcher matcher;
	ArrayList<Order> buyList = new ArrayList<Order>();
	ArrayList<Order> sellList = new ArrayList<Order>();
	ArrayList[] lists = new ArrayList[2];

	@GetMapping("/buyOrders")
	public ArrayList<Order> buyOrders(Matcher matcher) {
		this.buyList=matcher.buyList;
		return buyList;
	}

	@GetMapping("/sellOrders")
	public ArrayList<Order> sellOrders(Matcher matcher) {
		this.sellList=matcher.sellList;
		return sellList;
	}

	@PostMapping("/placeOrder")
	public  ArrayList[]  placeOrder (@RequestBody Order order, Matcher matcher) {
		matcher.processOrder(order);
		this.buyList = matcher.buyList;
		this.sellList = matcher.sellList;
		this.lists[0]=buyList;
		this.lists[1]=sellList;
		return lists;
	}
}
