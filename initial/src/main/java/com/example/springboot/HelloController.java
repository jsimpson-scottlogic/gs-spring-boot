package com.example.springboot;

import com.example.service.UserService;

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
import javax.validation.constraints.Size;
import java.lang.reflect.Array;
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

	ArrayList<Order> buyList = new ArrayList<Order>();
	ArrayList<Order> sellList = new ArrayList<Order>();
	ArrayList<Trade> tradeList = new ArrayList<Trade>();

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
	public ArrayList[] privateBook(@Size(min=1,message="Username cannot be null") @RequestBody String username){
		ArrayList[] privateBook= new ArrayList[3];
		ArrayList<Order> privateBuy=matcher.privateBuyList(username);
		ArrayList<Order> privateSell=matcher.privateSellList(username);
		ArrayList<Trade> privateTrade=matcher.privateTradeList(username);
		privateBook[0]=privateBuy;
		privateBook[1]=privateSell;
		privateBook[2]=privateTrade;
		return privateBook;
	}

	@PostMapping("/placeOrder")
	public  ArrayList[]  placeOrder (@Valid @RequestBody Order order){
		ArrayList[] lists = new ArrayList[2];
		matcher.processOrder(order);
		lists[0] = matcher.buyList;
		lists[1]= matcher.sellList;
		ArrayList[] privateBook=privateBook(order.getAccount());
		HashMap[] aggregateBook=aggregateBook();
		return lists;
	}

	@PostMapping("/addUser")
	private void addUser(@Valid @RequestBody User user){
		userService.add(user);
	}

	@PostMapping("/login")
	public String userLogin (@Valid @RequestBody User user) {
		String token = getJWTToken(user.getUsername());
		user.setToken(token);
		return user.getToken();

//		Login login= new Login();
//		List<String> users= userService.getAllUsernames();
//		List<String> passwords= userService.getAllPasswords();
//		boolean success=login.userLogin(user.getUsername(), user.getPassword(), users,passwords);
//		return success;
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
