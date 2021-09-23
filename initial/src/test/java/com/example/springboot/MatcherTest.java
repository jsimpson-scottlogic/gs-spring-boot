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
        Orders order1 = new Orders("Jessica", 10.50, 0, "buy");
        Orders order2 = new Orders("Jessica", 10.50, 2, "buy");
        ArrayList<Orders> list = new ArrayList<Orders>();
        list.add(order1);
        list.add(order2);
        assertEquals(2, list.size());
        matcher.removeCompletedMatches(list);
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("Remove all completed matches")
    void removeAllCompletedMatches() {
        Orders order1 = new Orders("Jessica", 10.50, 0, "buy");
        Orders order2 = new Orders("Jessica", 10.50, 0, "buy");
        ArrayList<Orders> list = new ArrayList<Orders>();
        list.add(order1);
        list.add(order2);
        assertEquals(2, list.size());
        matcher.removeCompletedMatches(list);
        assertEquals(0, list.size());
    }

    @Test
    @DisplayName("Orders added to buy list in descending order")
    void addToBuyList() {
        Orders order1 = new Orders("Jessica", 12.50, 10, "buy");
        matcher.processOrder(order1);
        Orders order2 = new Orders("Jacob", 10.50, 10, "buy");
        matcher.processOrder(order2);
        Orders order3 = new Orders("Jacob", 13.50, 10, "buy");
        matcher.processOrder(order3);
        Orders order4 = new Orders("James", 11.50, 10, "buy");
        matcher.processOrder(order4);
        assertEquals("Jacob", matcher.buyList.get(0).getAccount());
        assertEquals("Jessica", matcher.buyList.get(1).getAccount());
        assertEquals("James", matcher.buyList.get(2).getAccount());
        assertEquals("Jacob", matcher.buyList.get(3).getAccount());
    }

    @Test
    @DisplayName("Remove from buy list")
    void removeFromBuyList() {
        Orders order1 = new Orders("Jacob", 12.50, 10, "buy");
        matcher.buyList.add(order1);
        Orders order2 = new Orders("Jessica", 10.50, 10, "sell");
        matcher.processOrder(order2);
        assertEquals(0, matcher.buyList.size());
    }


    @Test
    @DisplayName("Orders added to sell list in descending order")
    void addToSellList() {
        Orders order1 = new Orders("Jessica", 12.50, 10, "sell");
        matcher.processOrder(order1);
        Orders order2 = new Orders("Jacob", 10.50, 10, "sell");
        matcher.processOrder(order2);
        Orders order3 = new Orders("James", 11.50, 10, "sell");
        matcher.processOrder(order3);
        assertEquals("Jacob", matcher.sellList.get(0).getAccount());
        assertEquals("James", matcher.sellList.get(1).getAccount());
        assertEquals("Jessica", matcher.sellList.get(2).getAccount());
    }

    @Test
    @DisplayName("Remove from sell list")
    void removeFromSellList() {
        Orders order1 = new Orders("Jacob", 10.50, 10, "sell");
        matcher.sellList.add(order1);
        Orders order2 = new Orders("Jessica", 12.50, 10, "buy");
        matcher.processOrder(order2);
        assertEquals(0, matcher.sellList.size());
    }

    @Test
    @DisplayName("Orders added to trade list")
    void addToTradeList() {
        Orders order1 = new Orders("Jessica", 12.50, 10, "buy");
        matcher.processOrder(order1);
        Orders order2 = new Orders("Jacob", 10.50, 10, "sell");
        matcher.processOrder(order2);
        assertEquals(1, matcher.tradeList.size());
    }

    @Test
    @DisplayName("Correct quantity added to trade list")
    void addQuantityToTradeList1() {
        Orders order1 = new Orders("Jessica", 12.50, 15, "buy");
        matcher.processOrder(order1);
        Orders order2 = new Orders("Jacob", 10.50, 10, "sell");
        matcher.processOrder(order2);
        assertEquals(10, matcher.tradeList.get(0).getQuantity());
    }

    @Test
    @DisplayName("Correct quantity added to trade list")
    void addQuantityToTradeList2() {
        Orders order1 = new Orders("Jessica", 12.50, 10, "buy");
        matcher.processOrder(order1);
        Orders order2 = new Orders("Jacob", 10.50, 15, "sell");
        matcher.processOrder(order2);
        assertEquals(10, matcher.tradeList.get(0).getQuantity());
    }

    @Test
    @DisplayName("Buy list quantity updated")
    void updateBuyList() {
        Orders order1 = new Orders("Jessica", 12.50, 15, "buy");
        matcher.processOrder(order1);
        Orders order2 = new Orders("Jacob", 10.50, 10, "sell");
        matcher.processOrder(order2);
        assertEquals(5, matcher.buyList.get(0).getAmount());
    }

    @Test
    @DisplayName("Sell list quantity updated")
    void updateSellList() {
        Orders order1 = new Orders("Jessica", 12.50, 15, "sell");
        matcher.processOrder(order1);
        Orders order2 = new Orders("Jacob", 14.50, 10, "buy");
        matcher.processOrder(order2);
        assertEquals(5, matcher.sellList.get(0).getAmount());
    }

    @Test
    @DisplayName("Trade doesn't happen when sell price too high")
    void priceTooHigh() {
        Orders order1 = new Orders("Jessica", 12.50, 15, "buy");
        matcher.processOrder(order1);
        Orders order2 = new Orders("Jacob", 14.50, 10, "sell");
        matcher.processOrder(order2);
        assertEquals(0, matcher.tradeList.size());
    }

    @Test
    @DisplayName("Trade doesn't happen when buy price too low")
    void priceTooLow() {
        Orders order1 = new Orders("Jacob", 14.50, 10, "sell");
        matcher.processOrder(order1);
        Orders order2 = new Orders("Jessica", 12.50, 15, "buy");
        matcher.processOrder(order2);
        assertEquals(0, matcher.tradeList.size());
    }

    @Test
    @DisplayName("Matches with multiple orders when amount large for buy order")
    void multipleTradesBuy() {
        Orders order1 = new Orders("Jessica", 15.50, 20, "buy");
        matcher.processOrder(order1);
        Orders order2 = new Orders("Jacob", 14.50, 10, "sell");
        matcher.processOrder(order2);
        Orders order3 = new Orders("James", 14.50, 5, "sell");
        matcher.processOrder(order3);
        assertEquals(2, matcher.tradeList.size());
        assertEquals(0, matcher.sellList.size());
        assertEquals(1, matcher.buyList.size());
    }

    @Test
    @DisplayName("Matches with multiple orders when amount large for buy order")
    void multipleTradesSell() {
        Orders order1 = new Orders("Jessica", 12.50, 20, "sell");
        matcher.processOrder(order1);
        Orders order2 = new Orders("Jacob", 14.50, 10, "buy");
        matcher.processOrder(order2);
        Orders order3 = new Orders("James", 14.50, 5, "buy");
        matcher.processOrder(order3);
        assertEquals(2, matcher.tradeList.size());
        assertEquals(1, matcher.sellList.size());
        assertEquals(0, matcher.buyList.size());
    }

    @Test
    @DisplayName("Check private buy list")
    void privateBuyList() {
        Orders order1 = new Orders("Jessica", 15.50, 20, "buy");
        matcher.processOrder(order1);
        Orders order2 = new Orders("Jessica", 19.50, 10, "buy");
        matcher.processOrder(order2);
        Orders order3 = new Orders("Jacob", 19.50, 10, "buy");
        matcher.processOrder(order3);
        ArrayList<Orders> privateBuyList = matcher.privateBuyList("Jessica");
        assertEquals(2, privateBuyList.size());
        privateBuyList = matcher.privateBuyList("Jacob");
        assertEquals(1, privateBuyList.size());
    }

    @Test
    @DisplayName("Check private sell list")
    void privateSellList() {
        Orders order1 = new Orders("Jessica", 15.50, 20, "sell");
        matcher.processOrder(order1);
        Orders order2 = new Orders("Jessica", 19.50, 10, "sell");
        matcher.processOrder(order2);
        Orders order3 = new Orders("Jacob", 19.50, 10, "sell");
        matcher.processOrder(order3);
        ArrayList<Orders> privateSellList = matcher.privateSellList("Jessica");
        assertEquals(2, privateSellList.size());
        privateSellList = matcher.privateSellList("Jacob");
        assertEquals(1, privateSellList.size());
    }

    @Test
    @DisplayName("Check private trade list")
    void privateTradeList() {
        Orders order1 = new Orders("Jessica", 15.50, 20, "sell");
        matcher.processOrder(order1);
        Orders order2 = new Orders("Jacob", 19.50, 10, "buy");
        matcher.processOrder(order2);
        ArrayList<Trade> privateTradeList = matcher.privateTradeList("Jessica");
        assertEquals("Jacob", privateTradeList.get(0).getAccount1());
        matcher.privateTradeList("Jacob");
        assertEquals("Jacob", privateTradeList.get(0).getAccount1());
    }

    @Test
    @DisplayName("Check aggregate buy")
    void aggregateBuy() {
        Orders order1 = new Orders("Jessica", 12.50, 15, "buy");
        matcher.processOrder(order1);
        Orders order2 = new Orders("Jacob", 12.50, 10, "buy");
        matcher.processOrder(order2);
        Orders order3 = new Orders("Jessica", 13.50, 15, "buy");
        matcher.processOrder(order3);
        Orders order4 = new Orders("Jacob", 13.50, 10, "buy");
        matcher.processOrder(order4);
        HashMap<Double, Integer> aggBuy = matcher.aggregateBuy();
        Object[] values = aggBuy.values().toArray();
        assertEquals(25, values[0]);
        assertEquals(25, values[1]);
    }

    @Test
    @DisplayName("Check aggregate sell when same price added")
    void aggregateSellAdded() {
        Orders order1 = new Orders("Jessica", 12.50, 15, "sell");
        matcher.processOrder(order1);
        Orders order2 = new Orders("Jacob", 12.50, 10, "sell");
        matcher.processOrder(order2);
        Orders order3 = new Orders("Jessica", 13.50, 15, "sell");
        matcher.processOrder(order3);
        Orders order4 = new Orders("Jacob", 13.50, 10, "sell");
        matcher.processOrder(order4);
        HashMap<Double, Integer> aggSell = matcher.aggregateSell();
        Object[] values = aggSell.values().toArray();
        assertEquals(25, values[0]);
        assertEquals(25, values[1]);
    }
}
