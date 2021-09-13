package com.example.springboot;

import java.util.ArrayList;

public class Matcher {

    private static ArrayList<Order> buyList = new ArrayList<Order>();
    private static ArrayList<Order> sellList = new ArrayList<Order>();
    private static ArrayList<Trade> tradeList = new ArrayList<Trade>();

    private static ArrayList<Order> privateBookSell = new ArrayList<Order>();
    private static ArrayList<Order> privateBookBuy = new ArrayList<Order>();
    private static ArrayList<Trade> privateTrade = new ArrayList<Trade>();

    private static final String BUY= "buy";
    private static final String SELL="sell";

    public static void createOrder(String account, double price, int amount, String action ) {
        Order newOrder= new Order(account,price,amount,action);
        processOrder(newOrder);
    }

    public static void processOrder(Order order){
        System.out.println(order.action);
        if (order.action==BUY){

            for (int i=0;i<sellList.size();i++){
                if (order.account==sellList.get(i).account){
                    System.out.println("User match");
                    continue;
                }else{
                    if(order.price>=sellList.get(i).price){
                        matchOrder(order,sellList.get(i));
                    }else{
                        System.out.println("No match");
                        break;
                    }
                }
                if (order.amount==0){
                    break;
                }
            }
            removeCompletedMatches(sellList);
            if (order.amount!=0){
                addOrder(order);
            }
        }else if (order.action==SELL){
            for (int i=0;i<buyList.size();i++){
                if (order.account==buyList.get(i).account){
                    continue;
                }else{
                    if(order.price<=buyList.get(i).price){
                        matchOrder(order,buyList.get(i));
                    }else{
                        break;
                    }
                }
                if (order.amount==0){
                    break;
                }
            }
            removeCompletedMatches(buyList);
            if (order.amount!=0){
                addOrder(order);
            }
        }
    }

    public static void matchOrder(Order order, Order matchedOrder){
        tradeHistory(order, matchedOrder);
        if (order.amount==matchedOrder.amount){
            order.amount=0;
            matchedOrder.amount=0;
        }else if (order.amount<=matchedOrder.amount){
            matchedOrder.amount=matchedOrder.amount-order.amount;
            order.amount=0;
        }else{
            order.amount=order.amount-matchedOrder.amount;
            matchedOrder.amount=0;
        }
    }

    public static void tradeHistory(Order order, Order matchedOrder){
        int quantity=Math.min(order.amount, matchedOrder.amount);
        Trade trade= new Trade(order.account, matchedOrder.account, matchedOrder.price , quantity, order.action);
        System.out.println(trade.account2);
        tradeList.add(trade);
    }

    public static void removeCompletedMatches(ArrayList<Order> list){
        for (int i=0; i<list.size();i++){
            if(list.get(i).amount==0){
                list.remove(i);
                i=i-1;
            }
        }
    }

    public static void addOrder(Order order){
        if (order.action==BUY){
            for (int i=0; i<buyList.size();i++){
                if (buyList.get(i).price>=order.price){
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
                if (sellList.get(i).price<=order.price){
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
            if (order.account==currentAccount){
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

    public static void main(String[] args){
        sellList.add(new Order("User1", 5.00,15,"sell"));
        sellList.add(new Order("User2", 5.00,15,"sell"));
        sellList.add(new Order("User2", 15.00,15,"sell"));
        createOrder("User1", 12.50,10,"buy");
        System.out.println(sellList.get(1).amount);
    }

}
