package com.example.springboot;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

public class MatcherTest {

    Matcher underTest;

    @BeforeEach
    public void setUpEach() {
           underTest = new Matcher();

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
        underTest.removeCompletedMatches(list);
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
        underTest.removeCompletedMatches(list);
        assertEquals(0, list.size());
    }

    @Test
    @DisplayName("Orders added to buy list in descending order")
    void addToBuyList(){
        Order order1=new Order("Jessica",12.50,10,"buy");
        underTest.processOrder(order1);
        Order order2=new Order("Jacob",10.50,10,"buy");
        underTest.processOrder(order2);
        Order order3=new Order("Jacob",13.50,10,"buy");
        underTest.processOrder(order3);
        Order order4=new Order("James",11.50,10,"buy");
        underTest.processOrder(order4);
        assertEquals("Jacob",underTest.buyList.get(0).getAccount());
        assertEquals("Jessica",underTest.buyList.get(1).getAccount());
        assertEquals("James",underTest.buyList.get(2).getAccount());
        assertEquals("Jacob",underTest.buyList.get(3).getAccount());
    }

    @Test
    @DisplayName("Remove from buy list")
    void removeFromBuyList(){
        Order order1= new Order("Jacob",12.50,10,"buy");
        underTest.buyList.add(order1);
        Order order2= new Order("Jessica",10.50,10,"sell");
        underTest.processOrder(order2);
        assertEquals(0, underTest.buyList.size());
    }


    @Test
    @DisplayName("Orders added to sell list in descending order")
    void addToSellList(){
        Order order1= new Order("Jessica",12.50,10,"sell");
        underTest.processOrder(order1);
        Order order2= new Order("Jacob",10.50,10,"sell");
        underTest.processOrder(order2);
        Order order3= new Order("James",11.50,10,"sell");
        underTest.processOrder(order3);
        assertEquals("Jacob",underTest.sellList.get(0).getAccount());
        assertEquals("James",underTest.sellList.get(1).getAccount());
        assertEquals("Jessica",underTest.sellList.get(2).getAccount());
    }

    @Test
    @DisplayName("Remove from sell list")
    void removeFromSellList(){
        Order order1= new Order("Jacob",10.50,10,"sell");
        underTest.sellList.add(order1);
        Order order2= new Order("Jessica",12.50,10,"buy");
        underTest.processOrder(order2);
        assertEquals(0, underTest.sellList.size());
    }

    @Test
    @DisplayName("Orders added to trade list")
    void addToTradeList(){
        Order order1= new Order("Jessica",12.50,10,"buy");
        underTest.processOrder(order1);
        Order order2= new Order("Jacob",10.50,10,"sell");
        underTest.processOrder(order2);
        assertEquals(1,underTest.tradeList.size());
    }

    @Test
    @DisplayName("Correct quantity added to trade list")
    void addQuantityToTradeList1(){
        Order order1= new Order("Jessica",12.50,15,"buy");
        underTest.processOrder(order1);
        Order order2= new Order("Jacob",10.50,10,"sell");
        underTest.processOrder(order2);
        assertEquals(10,underTest.tradeList.get(0).getQuantity());
    }

    @Test
    @DisplayName("Correct quantity added to trade list")
    void addQuantityToTradeList2(){
        Order order1= new Order("Jessica",12.50,10,"buy");
        underTest.processOrder(order1);
        Order order2= new Order("Jacob",10.50,15,"sell");
        underTest.processOrder(order2);
        assertEquals(10,underTest.tradeList.get(0).getQuantity());
    }

    @Test
    @DisplayName("Buy list quantity updated")
    void updateBuyList(){
        Order order1= new Order("Jessica",12.50,15,"buy");
        underTest.processOrder(order1);
        Order order2= new Order("Jacob",10.50,10,"sell");
        underTest.processOrder(order2);
        assertEquals(5,underTest.buyList.get(0).getAmount());
    }

    @Test
    @DisplayName("Sell list quantity updated")
    void updateSellList(){
        Order order1= new Order("Jessica",12.50,15,"sell");
        underTest.processOrder(order1);
        Order order2= new Order("Jacob",14.50,10,"buy");
        underTest.processOrder(order2);
        assertEquals(5,underTest.sellList.get(0).getAmount());
    }

    @Test
    @DisplayName("Trade doesn't happen when sell price too high")
    void priceTooHigh(){
        Order order1= new Order("Jessica",12.50,15,"buy");
        underTest.processOrder(order1);
        Order order2= new Order("Jacob",14.50,10,"sell");
        underTest.processOrder(order2);
        assertEquals(0,underTest.tradeList.size());
    }

    @Test
    @DisplayName("Trade doesn't happen when buy price too low")
    void priceTooLow(){
        Order order1= new Order("Jacob",14.50,10,"sell");
        underTest.processOrder(order1);
        Order order2= new Order("Jessica",12.50,15,"buy");
        underTest.processOrder(order2);
        assertEquals(0,underTest.tradeList.size());
    }

    @Test
    @DisplayName("Matches with multiple orders when amount large for buy order")
    void multipleTradesBuy(){
        Order order1= new Order("Jessica",15.50,20,"buy");
        underTest.processOrder(order1);
        Order order2= new Order("Jacob",14.50,10,"sell");
        underTest.processOrder(order2);
        Order order3= new Order("James",14.50,5,"sell");
        underTest.processOrder(order3);
        assertEquals(2,underTest.tradeList.size());
        assertEquals(0,underTest.sellList.size());
        assertEquals(1,underTest.buyList.size());
    }

    @Test
    @DisplayName("Matches with multiple orders when amount large for buy order")
    void multipleTradesSell(){
        Order order1= new Order("Jessica",12.50,20,"sell");
        underTest.processOrder(order1);
        Order order2= new Order("Jacob",14.50,10,"buy");
        underTest.processOrder(order2);
        Order order3= new Order("James",14.50,5,"buy");
        underTest.processOrder(order3);
        assertEquals(2,underTest.tradeList.size());
        assertEquals(1,underTest.sellList.size());
        assertEquals(0,underTest.buyList.size());
    }

    @Test
    @DisplayName("Check private buy list")
    void privateBuyList(){
        Order order1= new Order("Jessica",15.50,20,"buy");
        underTest.processOrder(order1);
        Order order2= new Order("Jessica",19.50,10,"buy");
        underTest.processOrder(order2);
        Order order3= new Order("Jacob",19.50,10,"buy");
        underTest.processOrder(order3);
        ArrayList<Order> privateBuyList=underTest.privateBuyList("Jessica");
        assertEquals(2,privateBuyList.size());
        privateBuyList=underTest.privateBuyList("Jacob");
        assertEquals(1,privateBuyList.size());
    }

    @Test
    @DisplayName("Check private sell list")
    void privateSellList(){
        Order order1= new Order("Jessica",15.50,20,"sell");
        underTest.processOrder(order1);
        Order order2= new Order("Jessica",19.50,10,"sell");
        underTest.processOrder(order2);
        Order order3= new Order("Jacob",19.50,10,"sell");
        underTest.processOrder(order3);
        ArrayList<Order> privateSellList=underTest.privateSellList("Jessica");
        assertEquals(2,privateSellList.size());
        privateSellList=underTest.privateSellList("Jacob");
        assertEquals(1,privateSellList.size());
    }

    @Test
    @DisplayName("Check private trade list")
    void privateTradeList(){
        Order order1= new Order("Jessica",15.50,20,"sell");
        underTest.processOrder(order1);
        Order order2= new Order("Jacob",19.50,10,"buy");
        underTest.processOrder(order2);
        ArrayList<Trade> privateTradeList=underTest.privateTradeList("Jessica");
        assertEquals("Jacob",privateTradeList.get(0).getAccount1());
        underTest.privateTradeList("Jacob");
        assertEquals("Jacob",privateTradeList.get(0).getAccount1());
    }

    @Test
    @DisplayName("Check aggregate buy")
    void aggregateBuy(){
        Order order1= new Order("Jessica",12.50,15,"buy");
        underTest.processOrder(order1);
        Order order2= new Order("Jacob",12.50,10,"buy");
        underTest.processOrder(order2);
        Order order3= new Order("Jessica",13.50,15,"buy");
        underTest.processOrder(order3);
        Order order4= new Order("Jacob",13.50,10,"buy");
        underTest.processOrder(order4);
        HashMap<Double,Integer> aggBuy=underTest.aggregateBuy();
        Object[] values=aggBuy.values().toArray();
        assertEquals(25,values[0]);
        assertEquals(25,values[1]);
    }

    @Test
    @DisplayName("Check aggregate sell when same price added")
    void aggregateSellAdded(){
        Order order1= new Order("Jessica",12.50,15,"sell");
        underTest.processOrder(order1);
        Order order2= new Order("Jacob",12.50,10,"sell");
        underTest.processOrder(order2);
        Order order3= new Order("Jessica",13.50,15,"sell");
        underTest.processOrder(order3);
        Order order4= new Order("Jacob",13.50,10,"sell");
        underTest.processOrder(order4);
        HashMap<Double,Integer> aggSell=underTest.aggregateSell();
        Object[] values=aggSell.values().toArray();
        assertEquals(25,values[0]);
        assertEquals(25,values[1]);
    }
}
