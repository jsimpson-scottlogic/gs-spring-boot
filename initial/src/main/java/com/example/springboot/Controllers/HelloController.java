package com.example.springboot.Controllers;

import com.example.service.OrderService;
import com.example.service.UserService;

import com.example.springboot.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
public class HelloController {

	@Autowired
	Matcher matcher;

	@Autowired
	Login login;

	@Autowired
	UserService userService;

	@Autowired
	OrderService orderService;

	@Autowired
	PlaceOrder placeOrder;

	@Autowired
	PlaceOrderBot placeOrderBot;

	String username;
	int orderId=0;

	@GetMapping("/buyOrders")
	public ArrayList<Orders> buyOrders() {
		return matcher.buyList;
	}

	@GetMapping("/sellOrders")
	public ArrayList<Orders> sellOrders() {
		return matcher.sellList;
	}

	@GetMapping("/allTrades")
	public ArrayList<Trade> allTrades(){
		return matcher.tradeList;
	}

	@GetMapping("/aggregateBook")
	public TreeMap[] aggregateBook(){
		TreeMap[] aggregateBook= new TreeMap[2];
		aggregateBook[0]=matcher.aggregateBuy();
		aggregateBook[1]=matcher.aggregateSell();
		return aggregateBook;
	}

	@GetMapping("/privateBook")
	public ArrayList[] privateBook(){
		ArrayList[] privateBook= new ArrayList[3];
		privateBook[0]=matcher.privateBuyList(username);
		privateBook[1]=matcher.privateSellList(username);
		privateBook[2]=matcher.privateTradeList(username);
		return privateBook;
	}

	@PostMapping("/placeOrder")
	public PlaceOrder placeOrder (@Valid @RequestBody OrderInfo orderinfo){
		Orders order=new Orders(username, orderinfo.getPrice(),orderinfo.getAmount(),orderinfo.getAction());
		ArrayList[] lists = new ArrayList[4];
		order.setId(orderId);
		orderId=orderId+1;
		orderService.save(order);
		matcher.processOrder(order);
		lists[0]=matcher.tradeList;
		ArrayList[] privateBook=privateBook();
		lists[1]=privateBook[0];
		lists[2]=privateBook[1];
		lists[3]=privateBook[2];
		TreeMap[] aggregateBook=aggregateBook();
		placeOrder.setLists(lists);
		placeOrder.setAggLists(aggregateBook);
		return placeOrder;
	}

	@PostMapping("/addUser" )
	private String addUser(@Valid @RequestBody User user){
		List<String> users= userService.getAllUsernames();
		if (users.contains(user.getUsername())){
			return "Username already taken";
		}else{
			userService.add(user);

			return "User added";
		}
	}

	@PostMapping("/login")
	public String userLogin (@Valid @RequestBody User user) {
		boolean success=login.userLogin(user, userService.getAllUsernames(),userService.getAllPasswords());
		if (success){
			username=user.getUsername();
			user.setToken(getJWTToken(user.getUsername()));
			userService.add(user);
			return user.getToken();
		}else{
			return "Invalid";
		}
	}

	@GetMapping("/allUsernames")
	public List<String> getUsernames(){
		return userService.getAllUsernames();
	}

	@GetMapping("/orderBot")
	public PlaceOrderBot orderBot(){
				List<String> usernames = userService.getAllUsernames();
				int b = (int) (Math.random() * (usernames.size()));
				username = usernames.get(b);
				double price = Math.round(((Math.random() * (100) + 1) * 100.0)) / 100.0;
				int amount = (int) (Math.random() * (100) + 1);
				int number = (int) Math.round(Math.random());
				String action = "buy";
				if (number == 0) {
					action = "buy";
				} else {
					action = "sell";
				}

				Orders order = new Orders(username, price, amount, action);
				ArrayList[] lists = new ArrayList[1];
				order.setId(orderId);
				orderId = orderId + 1;
				orderService.save(order);
				matcher.processOrder(order);
				lists[0] = matcher.tradeList;
				TreeMap[] aggregateBook = aggregateBook();
				placeOrderBot.setLists(lists);
				placeOrderBot.setAggLists(aggregateBook);
				return placeOrderBot;
	}

	private String getJWTToken(String username) {
		String secretKey = "mySecretKey";
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER");
		String token = Jwts
				.builder()
				.setId("softtekJWT")
				.setSubject(username)
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 600000))
				.signWith(SignatureAlgorithm.HS512,
						secretKey.getBytes()).compact();
		return "Bearer " + token;
	}
}
