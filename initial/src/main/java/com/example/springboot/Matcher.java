package com.example.springboot;

import java.awt.image.DataBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Matcher {

    public static ArrayList<Order> buyList = new ArrayList<Order>();
    public static ArrayList<Order> sellList = new ArrayList<Order>();
    public static ArrayList<Trade> tradeList = new ArrayList<Trade>();

    public static ArrayList<Order> privateBookSell = new ArrayList<Order>();
    public static ArrayList<Order> privateBookBuy = new ArrayList<Order>();
    public static ArrayList<Trade> privateTrade = new ArrayList<Trade>();

    public static HashMap<Double,Integer> aggBuy=new HashMap<Double,Integer>();
    public static HashMap<Double,Integer> aggSell=new HashMap<Double,Integer>();

    private static final String BUY= "buy";
    private static final String SELL="sell";

    public static void createOrder(String account, double price, int amount, String action ) {
        Order newOrder= new Order(account,price,amount,action);
        processOrder(newOrder);
    }

    public static void processOrder(Order order){
        if (order.getAction()==BUY){

            for (int i=0;i<sellList.size();i++){
                if (order.getAccount()==sellList.get(i).getAccount()){
                    System.out.println("User match");
                    continue;
                }else{
                    if(order.getPrice()>=sellList.get(i).getPrice()){
                        matchOrder(order,sellList.get(i));
                    }else{
                        System.out.println("No match");
                        break;
                    }
                }
                if (order.getAmount()==0){
                    break;
                }
            }
            removeCompletedMatches(sellList);
            if (order.getAmount()!=0){
                addOrder(order);
            }
        }else if (order.getAction()==SELL){
            for (int i=0;i<buyList.size();i++){
                if (order.getAccount()==buyList.get(i).getAccount()){
                    continue;
                }else{
                    if(order.getPrice()<=buyList.get(i).getPrice()){
                        matchOrder(order,buyList.get(i));
                    }else{
                        break;
                    }
                }
                if (order.getAmount()==0){
                    break;
                }
            }
            removeCompletedMatches(buyList);
            if (order.getAmount()!=0){
                addOrder(order);
            }
        }
    }

    public static void matchOrder(Order order, Order matchedOrder){
        tradeHistory(order, matchedOrder);
        if (order.getAmount()==matchedOrder.getAmount()){
            order.setAmount(0);
            matchedOrder.setAmount(0);
        }else if (order.getAmount()<=matchedOrder.getAmount()){
            matchedOrder.setAmount(matchedOrder.getAmount()-order.getAmount());
            order.setAmount(0);
        }else{
            order.setAmount(order.getAmount()-matchedOrder.getAmount());
            matchedOrder.setAmount(0);
        }
    }

    public static void tradeHistory(Order order, Order matchedOrder){
        int quantity=Math.min(order.getAmount(), matchedOrder.getAmount());
        Trade trade= new Trade(order.getAccount(), matchedOrder.getAccount(), matchedOrder.getPrice(), quantity, order.getAction());
        tradeList.add(trade);
    }

    public static void removeCompletedMatches(ArrayList<Order> list){
        for (int i=0; i<list.size();i++){
            if(list.get(i).getAmount()==0){
                list.remove(i);
                i=i-1;
            }
        }
    }

    public static void addOrder(Order order){
        if (order.getAction()==BUY){
            for (int i=0; i<buyList.size();i++){
                if (buyList.get(i).getPrice()>=order.getPrice()){
                    int len=buyList.size()-1;
                    if(i==len){
                        buyList.add(order);
                        break;
                    }else{
                        continue;
                    }
                }else{
                    buyList.add(i,order);
                    break;
                }
            }
            if (buyList.size()==0){
                buyList.add(order);
            }
        }else{
            for (int i=0; i<sellList.size();i++){
                if (sellList.get(i).getPrice()<=order.getPrice()){
                    int len=sellList.size()-1;
                    if(i==len){
                        sellList.add(order);
                        break;
                    }else{
                        continue;
                    }
                }else{
                    sellList.add(i,order);
                    break;
                }
            }
            if (sellList.size()==0){
                sellList.add(order);
            }
        }
    }

     public static void privateOrderList(String currentAccount, ArrayList<Order> list, ArrayList<Order> privateList){
        for (int i=0;i<list.size();i++){
            Order order=list.get(i);
            if (order.getAccount()==currentAccount){
                privateList.add(order);
            }
        }
    }

    public static void privateTradeList(String currentAccount){
        for (int i=0;i<tradeList.size();i++){
            Trade trade=tradeList.get(i);
            if (trade.account1==currentAccount || trade.account2==currentAccount){
                privateTrade.add(trade);
            }
        }
    }


    public static void aggregateBuy(){
        for (int i=0;i<buyList.size();i++){
            double price =buyList.get(i).getPrice();
            if (aggBuy.containsKey(price)){
                int quantity =aggBuy.get(price) +buyList.get(i).getAmount();
                aggBuy.put(price, quantity);
            }else{
                aggBuy.put(price, buyList.get(i).getAmount());
            }
        }
    }

    public static void aggregateSell(){
        for (int i=0;i< sellList.size();i++){
            double price =sellList.get(i).getPrice();
            if (aggSell.containsKey(price)){
                int quantity =aggSell.get(price) +sellList.get(i).getAmount();
                aggSell.put(price, quantity);
            }else{
                aggSell.put(price, sellList.get(i).getAmount());
            }
        }
    }

    public static void main(String[] args){
        createOrder("Jessica",12.50,15,"buy");
        createOrder("Jacob",12.50,10,"buy");
        createOrder("Jessica",13.50,15,"buy");
        createOrder("Jacob",13.50,10,"buy");
        aggregateBuy();
        Object[] keys=aggBuy.values().toArray();
        System.out.println(keys[1]);
    }

}
