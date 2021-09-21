package com.example.springboot;

import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.lang.reflect.Array;
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
	ArrayList<Trade> tradeList = new ArrayList<Trade>();

	@GetMapping("/")
	public String index() {
		return "Welcome to the trading app!";
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
	public  ArrayList<Order>[] placeOrder (@Valid @RequestBody Order order){
		ArrayList<Order>[] lists = new ArrayList[2];
		matcher.processOrder(order);
		lists[0] = matcher.buyList;
		lists[1]= matcher.sellList;
		ArrayList[] privateBook=privateBook(order.getAccount());
		HashMap[] aggregateBook=aggregateBook();
		return lists;
	}

	@PostMapping("/login")
	public String userLogin (@Valid @RequestParam("username") String username, @RequestParam("password") String password) {
		List<String> users= userService.getAllUsernames();
		List<String> passwords= userService.getAllPasswords();
		return login.userLogin(username, password, users,passwords);
	}

	@PostMapping("/addUser")
	private void addUser(@Valid @RequestBody User user){
		userService.add(user);
	}
}
