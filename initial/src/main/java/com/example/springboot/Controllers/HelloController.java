package com.example.springboot.Controllers;

import com.example.service.OrderService;
import com.example.service.UserService;

import com.example.springboot.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

import java.util.*;
import java.util.stream.Collectors;

@RestController
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

	ArrayList<Orders> buyList = new ArrayList<Orders>();
	ArrayList<Orders> sellList = new ArrayList<Orders>();
	ArrayList<Trade> tradeList = new ArrayList<Trade>();
	String username;
	int orderId=0;

	@GetMapping("/buyOrders")
	public ArrayList<Orders> buyOrders() {
		buyList=matcher.buyList;
		return buyList;
	}

	@GetMapping("/sellOrders")
	public ArrayList<Orders> sellOrders() {
		sellList=matcher.sellList;
		return sellList;
	}

	@GetMapping("/allTrades")
	public ArrayList<Trade> allTrades(){
		tradeList=matcher.tradeList;
		return tradeList;
	}

	@GetMapping("/aggregateBook")
	public HashMap[] aggregateBook(){
		HashMap<Double,Integer> aggregateBuy= matcher.aggregateBuy();
		HashMap<Double,Integer> aggregateSell= matcher.aggregateSell();
		HashMap[] aggregateBook= new HashMap[2];
		aggregateBook[0]=aggregateBuy;
		aggregateBook[1]=aggregateSell;
		return aggregateBook;
	}

	@GetMapping("/privateBook")
	public ArrayList[] privateBook(){
		ArrayList[] privateBook= new ArrayList[3];
		ArrayList<Orders> privateBuy=matcher.privateBuyList(username);
		ArrayList<Orders> privateSell=matcher.privateSellList(username);
		ArrayList<Trade> privateTrade=matcher.privateTradeList(username);
		privateBook[0]=privateBuy;
		privateBook[1]=privateSell;
		privateBook[2]=privateTrade;
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
		HashMap[] aggregateBook=aggregateBook();
		placeOrder.setLists(lists);
		placeOrder.setAggLists(aggregateBook);
		return placeOrder;
	}

	@PostMapping("/addUser")
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
	public String userLogin (@Valid @RequestBody User user) throws Exception {
		Login login= new Login();
		List<String> users= userService.getAllUsernames();
		List<String> passwords= userService.getAllPasswords();
		username=user.getUsername();
		boolean success=login.userLogin(username, user.getPassword(), users,passwords);
		if (success==true){
			String token = getJWTToken(user.getUsername());
			user.setToken(token);
			userService.add(user);
			return user.getToken();
		}else{
			username="";
			return "Invalid";
		}
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
