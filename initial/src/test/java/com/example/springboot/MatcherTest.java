package com.example.springboot;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

public class MatcherTest {

    Matcher underTest = new Matcher();

    @BeforeEach
    public void setUpEach() {
         underTest.buyList.clear();
         underTest.sellList.clear();
         underTest.tradeList.clear();

         underTest.privateBookBuy.clear();
         underTest.privateBookSell.clear();
         underTest.privateTrade.clear();

         underTest.aggBuy.clear();
         underTest.aggSell.clear();
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
        underTest.createOrder("Jessica",12.50,10,"buy");
        underTest.createOrder("Jacob",10.50,10,"buy");
        underTest.createOrder("James",11.50,10,"buy");
        assertEquals("Jessica",underTest.buyList.get(0).getAccount());
        assertEquals("James",underTest.buyList.get(1).getAccount());
        assertEquals("Jacob",underTest.buyList.get(2).getAccount());
    }

    @Test
    @DisplayName("Remove from buy list")
    void removeFromBuyList(){
        Order order= new Order("Jacob",12.50,10,"buy");
        underTest.buyList.add(order);
        underTest.createOrder("Jessica",10.50,10,"sell");
        assertEquals(0, underTest.buyList.size());
    }

    @Test
    @DisplayName("Orders added to sell list in descending order")
    void addToSellList(){
        underTest.createOrder("Jessica",12.50,10,"sell");
        underTest.createOrder("Jacob",10.50,10,"sell");
        underTest.createOrder("James",11.50,10,"sell");
        assertEquals("Jacob",underTest.sellList.get(0).getAccount());
        assertEquals("James",underTest.sellList.get(1).getAccount());
        assertEquals("Jessica",underTest.sellList.get(2).getAccount());
    }

    @Test
    @DisplayName("Remove from sell list")
    void removeFromSellList(){
        Order order= new Order("Jacob",10.50,10,"sell");
        underTest.sellList.add(order);
        underTest.createOrder("Jessica",12.50,10,"buy");
        assertEquals(0, underTest.sellList.size());
    }

    @Test
    @DisplayName("Orders added to trade list")
    void addToTradeList(){
        underTest.createOrder("Jessica",12.50,10,"buy");
        underTest.createOrder("Jacob",10.50,10,"sell");
        assertEquals(1,underTest.tradeList.size());
    }

    @Test
    @DisplayName("Correct quantity added to trade list")
    void addQuantityToTradeList1(){
        underTest.createOrder("Jessica",12.50,15,"buy");
        underTest.createOrder("Jacob",10.50,10,"sell");
        assertEquals(10,underTest.tradeList.get(0).getQuantity());
    }

    @Test
    @DisplayName("Correct quantity added to trade list")
    void addQuantityToTradeList2(){
        underTest.createOrder("Jessica",12.50,10,"buy");
        underTest.createOrder("Jacob",10.50,15,"sell");
        assertEquals(10,underTest.tradeList.get(0).getQuantity());
    }

    @Test
    @DisplayName("Buy list quantity updated")
    void updateBuyList(){
        underTest.createOrder("Jessica",12.50,15,"buy");
        underTest.createOrder("Jacob",10.50,10,"sell");
        assertEquals(5,underTest.buyList.get(0).getAmount());
    }

    @Test
    @DisplayName("Sell list quantity updated")
    void updateSellList(){
        underTest.createOrder("Jessica",12.50,15,"sell");
        underTest.createOrder("Jacob",14.50,10,"buy");
        assertEquals(5,underTest.sellList.get(0).getAmount());
    }

    @Test
    @DisplayName("Trade doesn't happen when sell price too high")
    void priceTooHigh(){
        underTest.createOrder("Jessica",12.50,15,"buy");
        underTest.createOrder("Jacob",14.50,10,"sell");
        assertEquals(1,underTest.buyList.size());
    }

    @Test
    @DisplayName("Matches with multiple orders when amount large")
    void multipleTrades(){
        underTest.createOrder("Jessica",15.50,20,"buy");
        underTest.createOrder("Jacob",14.50,10,"sell");
        underTest.createOrder("James",14.50,5,"sell");
        assertEquals(2,underTest.tradeList.size());
        assertEquals(0,underTest.sellList.size());
        assertEquals(1,underTest.buyList.size());
    }

    @Test
    @DisplayName("Check private buy list")
    void privateBuyList(){
        underTest.createOrder("Jessica",15.50,20,"buy");
        underTest.createOrder("Jessica",19.50,10,"buy");
        underTest.privateOrderList("Jessica", underTest.buyList, underTest.privateBookBuy);
        assertEquals(2,underTest.privateBookBuy.size());
    }

    @Test
    @DisplayName("Check private trade list")
    void privateTradeList(){
        underTest.createOrder("Jessica",15.50,20,"sell");
        underTest.createOrder("Jacob",19.50,10,"buy");
        underTest.privateTradeList("Jessica");
        assertEquals("Jacob",underTest.privateTrade.get(0).getAccount1());
    }


    @Test
    @DisplayName("Check aggregate buy")
    void aggregateBuy(){
        underTest.createOrder("Jessica",12.50,15,"buy");
        underTest.createOrder("Jacob",12.50,10,"buy");
        underTest.createOrder("Jessica",13.50,15,"buy");
        underTest.createOrder("Jacob",13.50,10,"buy");
        underTest.aggregateBuy();
        Object[] values=underTest.aggBuy.values().toArray();
        assertEquals(25,values[0]);
        assertEquals(25,values[1]);
    }

    @Test
    @DisplayName("Check aggregate sell")
    void aggregateSell(){
        underTest.createOrder("Jessica",12.50,15,"sell");
        underTest.createOrder("Jacob",12.50,10,"sell");
        underTest.createOrder("Jessica",13.50,15,"sell");
        underTest.createOrder("Jacob",13.50,10,"sell");
        underTest.aggregateSell();
        Object[] values=underTest.aggSell.values().toArray();
        assertEquals(25,values[0]);
        assertEquals(25,values[1]);
    }


}
