package com.example.springboot;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

public class MatcherTest {

    Matcher matcher;

    @BeforeEach
    public void setUpEach() {
           matcher = new Matcher();

    }

    @Test
    @DisplayName("Remove completed matches")
    void removeCompletedMatches() {
        Order order1 = new Order("Jessica", 10.50, 0, "buy");
        Order order2 = new Order("Jessica", 10.50, 2, "buy");
        ArrayList<Order> list = new ArrayList<Order>();
        list.add(order1);
        list.add(order2);
        assertEquals(2, list.size());
        matcher.removeCompletedMatches(list);
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("Remove all completed matches")
    void removeAllCompletedMatches() {
        Order order1 = new Order("Jessica", 10.50, 0, "buy");
        Order order2 = new Order("Jessica", 10.50, 0, "buy");
        ArrayList<Order> list = new ArrayList<Order>();
        list.add(order1);
        list.add(order2);
        assertEquals(2, list.size());
        matcher.removeCompletedMatches(list);
        assertEquals(0, list.size());
    }

    @Test
    @DisplayName("Orders added to buy list in descending order")
    void addToBuyList(){
        Order order1=new Order("Jessica",12.50,10,"buy");
        matcher.processOrder(order1);
        Order order2=new Order("Jacob",10.50,10,"buy");
        matcher.processOrder(order2);
        Order order3=new Order("Jacob",13.50,10,"buy");
        matcher.processOrder(order3);
        Order order4=new Order("James",11.50,10,"buy");
        matcher.processOrder(order4);
        assertEquals("Jacob",matcher.buyList.get(0).getAccount());
        assertEquals("Jessica",matcher.buyList.get(1).getAccount());
        assertEquals("James",matcher.buyList.get(2).getAccount());
        assertEquals("Jacob",matcher.buyList.get(3).getAccount());
    }

    @Test
    @DisplayName("Remove from buy list")
    void removeFromBuyList(){
        Order order1= new Order("Jacob",12.50,10,"buy");
        matcher.buyList.add(order1);
        Order order2= new Order("Jessica",10.50,10,"sell");
        matcher.processOrder(order2);
        assertEquals(0, matcher.buyList.size());
    }


    @Test
    @DisplayName("Orders added to sell list in descending order")
    void addToSellList(){
        Order order1= new Order("Jessica",12.50,10,"sell");
        matcher.processOrder(order1);
        Order order2= new Order("Jacob",10.50,10,"sell");
        matcher.processOrder(order2);
        Order order3= new Order("James",11.50,10,"sell");
        matcher.processOrder(order3);
        assertEquals("Jacob",matcher.sellList.get(0).getAccount());
        assertEquals("James",matcher.sellList.get(1).getAccount());
        assertEquals("Jessica",matcher.sellList.get(2).getAccount());
    }

    @Test
    @DisplayName("Remove from sell list")
    void removeFromSellList(){
        Order order1= new Order("Jacob",10.50,10,"sell");
        matcher.sellList.add(order1);
        Order order2= new Order("Jessica",12.50,10,"buy");
        matcher.processOrder(order2);
        assertEquals(0, matcher.sellList.size());
    }

    @Test
    @DisplayName("Orders added to trade list")
    void addToTradeList(){
        Order order1= new Order("Jessica",12.50,10,"buy");
        matcher.processOrder(order1);
        Order order2= new Order("Jacob",10.50,10,"sell");
        matcher.processOrder(order2);
        assertEquals(1,matcher.tradeList.size());
    }

    @Test
    @DisplayName("Correct quantity added to trade list")
    void addQuantityToTradeList1(){
        Order order1= new Order("Jessica",12.50,15,"buy");
        matcher.processOrder(order1);
        Order order2= new Order("Jacob",10.50,10,"sell");
        matcher.processOrder(order2);
        assertEquals(10,matcher.tradeList.get(0).getQuantity());
    }

    @Test
    @DisplayName("Correct quantity added to trade list")
    void addQuantityToTradeList2(){
        Order order1= new Order("Jessica",12.50,10,"buy");
        matcher.processOrder(order1);
        Order order2= new Order("Jacob",10.50,15,"sell");
        matcher.processOrder(order2);
        assertEquals(10,matcher.tradeList.get(0).getQuantity());
    }

    @Test
    @DisplayName("Buy list quantity updated")
    void updateBuyList(){
        Order order1= new Order("Jessica",12.50,15,"buy");
        matcher.processOrder(order1);
        Order order2= new Order("Jacob",10.50,10,"sell");
        matcher.processOrder(order2);
        assertEquals(5,matcher.buyList.get(0).getAmount());
    }

    @Test
    @DisplayName("Sell list quantity updated")
    void updateSellList(){
        Order order1= new Order("Jessica",12.50,15,"sell");
        matcher.processOrder(order1);
        Order order2= new Order("Jacob",14.50,10,"buy");
        matcher.processOrder(order2);
        assertEquals(5,matcher.sellList.get(0).getAmount());
    }

    @Test
    @DisplayName("Trade doesn't happen when sell price too high")
    void priceTooHigh(){
        Order order1= new Order("Jessica",12.50,15,"buy");
        matcher.processOrder(order1);
        Order order2= new Order("Jacob",14.50,10,"sell");
        matcher.processOrder(order2);
        assertEquals(0,matcher.tradeList.size());
    }

    @Test
    @DisplayName("Trade doesn't happen when buy price too low")
    void priceTooLow(){
        Order order1= new Order("Jacob",14.50,10,"sell");
        matcher.processOrder(order1);
        Order order2= new Order("Jessica",12.50,15,"buy");
        matcher.processOrder(order2);
        assertEquals(0,matcher.tradeList.size());
    }

    @Test
    @DisplayName("Matches with multiple orders when amount large for buy order")
    void multipleTradesBuy(){
        Order order1= new Order("Jessica",15.50,20,"buy");
        matcher.processOrder(order1);
        Order order2= new Order("Jacob",14.50,10,"sell");
        matcher.processOrder(order2);
        Order order3= new Order("James",14.50,5,"sell");
        matcher.processOrder(order3);
        assertEquals(2,matcher.tradeList.size());
        assertEquals(0,matcher.sellList.size());
        assertEquals(1,matcher.buyList.size());
    }

    @Test
    @DisplayName("Matches with multiple orders when amount large for buy order")
    void multipleTradesSell(){
        Order order1= new Order("Jessica",12.50,20,"sell");
        matcher.processOrder(order1);
        Order order2= new Order("Jacob",14.50,10,"buy");
        matcher.processOrder(order2);
        Order order3= new Order("James",14.50,5,"buy");
        matcher.processOrder(order3);
        assertEquals(2,matcher.tradeList.size());
        assertEquals(1,matcher.sellList.size());
        assertEquals(0,matcher.buyList.size());
    }

    @Test
    @DisplayName("Check private buy list")
    void privateBuyList(){
        Order order1= new Order("Jessica",15.50,20,"buy");
        matcher.processOrder(order1);
        Order order2= new Order("Jessica",19.50,10,"buy");
        matcher.processOrder(order2);
        Order order3= new Order("Jacob",19.50,10,"buy");
        matcher.processOrder(order3);
        ArrayList<Order> privateBuyList=matcher.privateBuyList("Jessica");
        assertEquals(2,privateBuyList.size());
        privateBuyList=matcher.privateBuyList("Jacob");
        assertEquals(1,privateBuyList.size());
    }

    @Test
    @DisplayName("Check private sell list")
    void privateSellList(){
        Order order1= new Order("Jessica",15.50,20,"sell");
        matcher.processOrder(order1);
        Order order2= new Order("Jessica",19.50,10,"sell");
        matcher.processOrder(order2);
        Order order3= new Order("Jacob",19.50,10,"sell");
        matcher.processOrder(order3);
        ArrayList<Order> privateSellList=matcher.privateSellList("Jessica");
        assertEquals(2,privateSellList.size());
        privateSellList=matcher.privateSellList("Jacob");
        assertEquals(1,privateSellList.size());
    }

    @Test
    @DisplayName("Check private trade list")
    void privateTradeList(){
        Order order1= new Order("Jessica",15.50,20,"sell");
        matcher.processOrder(order1);
        Order order2= new Order("Jacob",19.50,10,"buy");
        matcher.processOrder(order2);
        ArrayList<Trade> privateTradeList=matcher.privateTradeList("Jessica");
        assertEquals("Jacob",privateTradeList.get(0).getAccount1());
        matcher.privateTradeList("Jacob");
        assertEquals("Jacob",privateTradeList.get(0).getAccount1());
    }

    @Test
    @DisplayName("Check aggregate buy")
    void aggregateBuy(){
        Order order1= new Order("Jessica",12.50,15,"buy");
        matcher.processOrder(order1);
        Order order2= new Order("Jacob",12.50,10,"buy");
        matcher.processOrder(order2);
        Order order3= new Order("Jessica",13.50,15,"buy");
        matcher.processOrder(order3);
        Order order4= new Order("Jacob",13.50,10,"buy");
        matcher.processOrder(order4);
        HashMap<Double,Integer> aggBuy=matcher.aggregateBuy();
        Object[] values=aggBuy.values().toArray();
        assertEquals(25,values[0]);
        assertEquals(25,values[1]);
    }

    @Test
    @DisplayName("Check aggregate sell when same price added")
    void aggregateSellAdded(){
        Order order1= new Order("Jessica",12.50,15,"sell");
        matcher.processOrder(order1);
        Order order2= new Order("Jacob",12.50,10,"sell");
        matcher.processOrder(order2);
        Order order3= new Order("Jessica",13.50,15,"sell");
        matcher.processOrder(order3);
        Order order4= new Order("Jacob",13.50,10,"sell");
        matcher.processOrder(order4);
        HashMap<Double,Integer> aggSell=matcher.aggregateSell();
        Object[] values=aggSell.values().toArray();
        assertEquals(25,values[0]);
        assertEquals(25,values[1]);
    }

    @Test
    @DisplayName("Price validation")
    void validatePrice(){
        Order order1= new Order("Jessica",0.00,10,"buy");
        matcher.processOrder(order1);
        assertEquals(0,matcher.buyList.size());
    }
}
