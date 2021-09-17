package com.example.springboot;

import com.example.service.UserService;
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

	@Autowired
	Login login;

	@Autowired
	UserService userService;

	ArrayList<Order> buyList = new ArrayList<Order>();
	ArrayList<Order> sellList = new ArrayList<Order>();

	@GetMapping("/")
	public String index() {
		String sentence = "Welcome to the trading app!";
		return sentence;
	}

	@GetMapping("/buyOrders")
	public ArrayList<Order> buyOrders() {
		buyList=matcher.buyList;
		return buyList;
	}

	@GetMapping("/sellOrders")
	public ArrayList<Order> sellOrders() {
		sellList=matcher.sellList;
		return sellList;
	}

	@PostMapping("/placeOrder")
	public  ArrayList[]  placeOrder (@Valid @RequestBody Order order){
		ArrayList[] lists = new ArrayList[2];
		matcher.processOrder(order);
		buyList = matcher.buyList;
		sellList = matcher.sellList;
		lists[0]=buyList;
		lists[1]=sellList;
		return lists;
	}

	@PostMapping("/login")
	public String userLogin (@Valid @RequestBody User user) {
		Login login= new Login();
		String success=login.userLogin(user.getUsername(), user.getPassword());
		return success;
	}

	@PostMapping("/addUser")
	private int addUser(@RequestBody User user){
		userService.add(user);
		return user.getId();
	}
}
