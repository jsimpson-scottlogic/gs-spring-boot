package com.example.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.*;

@RestController
public class HelloController {

	@Autowired
	Matcher matcher;
	ArrayList<Order> buyList = new ArrayList<Order>();
	ArrayList<Order> sellList = new ArrayList<Order>();

	@GetMapping("/buyOrders")
	public ArrayList<Order> buyOrders(Matcher matcher) {
		buyList=matcher.buyList;
		return buyList;
	}

	@GetMapping("/sellOrders")
	public ArrayList<Order> sellOrders(Matcher matcher) {
		sellList=matcher.sellList;
		return sellList;
	}

	@PostMapping("/placeOrder")
	public  ArrayList[]  placeOrder (@Valid @RequestBody Order order, Matcher matcher) {
		ArrayList[] lists = new ArrayList[2];
		matcher.processOrder(order);
		buyList = matcher.buyList;
		sellList = matcher.sellList;
		lists[0]=buyList;
		lists[1]=sellList;
		return lists;
	}

	@PostMapping("/login")
	public boolean userLogin (@RequestBody User user) {
		Login login=new Login();
		boolean success=login.userLogin(user.getUsername(), user.getPassword());
		return success;
	}

}
