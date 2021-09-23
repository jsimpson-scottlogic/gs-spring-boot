package com.example.springboot;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;

@Component
public class Matcher {

    //Attributes
    public ArrayList<Orders> buyList ;
    public ArrayList<Orders> sellList;
    public ArrayList<Trade> tradeList;

    public String BUY="buy";
    public String SELL="sell";

    //Constructor
    public Matcher(){
        this.buyList = new ArrayList<Orders>();
        this.sellList = new ArrayList<Orders>();
        this.tradeList = new ArrayList<Trade>();
    };

    //Methods
    public void processOrder(Orders order){
        if (order.getAction().equals(BUY)){
            for (Orders value : sellList) {
                if (order.getAccount().equals(value.getAccount())) {
                    continue;
                } else {
                    if (order.getPrice() >= value.getPrice()) {
                        matchOrder(order, value);
                    } else {
                        break;
                    }
                }
                if (order.getAmount() == 0) {
                    break;
                }
            }
            removeCompletedMatches(sellList);
            if (order.getAmount()!=0){
                addOrder(order);
            }
        }else if (order.getAction().equals(SELL)){
            for (Orders value : buyList) {
                if (order.getAccount().equals(value.getAccount())) {
                    continue;
                } else {
                    if (order.getPrice() <= value.getPrice()) {
                        matchOrder(order, value);
                    } else {
                        break;
                    }
                }
                if (order.getAmount() == 0) {
                    break;
                }
            }
            removeCompletedMatches(buyList);
            if (order.getAmount()!=0){
                addOrder(order);
            }
        }
    }

    public void matchOrder(Orders order, Orders matchedOrder){
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

    public void tradeHistory(Orders order, Orders matchedOrder){
        int quantity=Math.min(order.getAmount(), matchedOrder.getAmount());
        Trade trade= new Trade(order.getAccount(), matchedOrder.getAccount(), matchedOrder.getPrice(), quantity, order.getAction());
        tradeList.add(trade);
    }

    public void removeCompletedMatches(ArrayList<Orders> list){
        for (int i=0; i<list.size();i++){
            if(list.get(i).getAmount()==0){
                list.remove(i);
                i=i-1;
            }
        }
    }

    public void addOrder(Orders order) {
        if (order.getAction().equals(BUY)) {
            for (int i = 0; i < buyList.size(); i++) {
                if (buyList.get(i).getPrice() >= order.getPrice()) {
                    int len = buyList.size() - 1;
                    if (i == len) {
                        buyList.add(order);
                        break;
                    } else {
                        continue;
                    }
                } else {
                    buyList.add(i, order);
                    break;
                }
            }
            if (buyList.size() == 0) {
                buyList.add(order);
            }
        } else if (order.getAction().equals(SELL)) {
            for (int i = 0; i < sellList.size(); i++) {
                if (sellList.get(i).getPrice() <= order.getPrice()) {
                    int len = sellList.size() - 1;
                    if (i == len) {
                        sellList.add(order);
                        break;
                    } else {
                        continue;
                    }
                } else {
                    sellList.add(i, order);
                    break;
                }
            }
            if (sellList.size() == 0) {
                sellList.add(order);
            }
        }
    }

     public ArrayList<Orders> privateBuyList(String currentAccount){
        ArrayList<Orders> privateBuyList= new ArrayList<Orders>();
         for (Orders order : buyList) {
             if (order.getAccount().equals(currentAccount)) {
                 privateBuyList.add(order);
             }
         }
        return privateBuyList;
     }

    public ArrayList<Orders> privateSellList(String currentAccount){
        ArrayList<Orders> privateSellList= new ArrayList<Orders>();
        for (Orders order : sellList) {
            if (order.getAccount().equals(currentAccount)) {
                privateSellList.add(order);
            }
        }
        return privateSellList;
    }

    public ArrayList<Trade> privateTradeList(String currentAccount){
        ArrayList<Trade> privateTrade= new ArrayList<Trade>();
        for (Trade trade : tradeList) {
            if (trade.account1 == currentAccount || trade.account2 == currentAccount) {
                privateTrade.add(trade);
            }
        }
        return privateTrade;
    }

    public HashMap<Double,Integer> aggregateBuy(){
        HashMap<Double,Integer> aggBuy=new HashMap<Double,Integer>();
        for (Orders order : buyList) {
            double price = order.getPrice();
            if (aggBuy.containsKey(price)) {
                int quantity = aggBuy.get(price) + order.getAmount();
                aggBuy.put(price, quantity);
            } else {
                aggBuy.put(price, order.getAmount());
            }
        }
        return aggBuy;
    }

    public HashMap<Double,Integer> aggregateSell(){
        HashMap<Double,Integer> aggSell=new HashMap<Double,Integer>();
        for (Orders order : sellList) {
            double price = order.getPrice();
            if (aggSell.containsKey(price)) {
                int quantity = aggSell.get(price) + order.getAmount();
                aggSell.put(price, quantity);
            } else {
                aggSell.put(price, order.getAmount());
            }
        }
        return aggSell;
    }
}
