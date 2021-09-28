package com.example.springboot;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;

@Component
public class PlaceOrder {

    private ArrayList[] lists;
    private HashMap[] aggLists;

    public PlaceOrder(ArrayList[] lists, HashMap[] aggLists){
        this.lists=lists;
        this.aggLists=aggLists;
    }

    public PlaceOrder(){}

    public ArrayList[] getLists() {
        return lists;
    }

    public void setLists(ArrayList[] lists) {
        this.lists = lists;
    }

    public HashMap[] getAggLists() {
        return aggLists;
    }

    public void setAggLists(HashMap[] aggLists) {
        this.aggLists = aggLists;
    }

}
