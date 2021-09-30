package com.example.springboot;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

@Component
public class PlaceOrderBot {

    private ArrayList[] lists;
    private TreeMap[] aggLists;

    public PlaceOrderBot(ArrayList[] lists, TreeMap[] aggLists){
        this.lists=lists;
        this.aggLists=aggLists;
    }

    public PlaceOrderBot(){}

    public ArrayList[] getLists() {
        return lists;
    }

    public void setLists(ArrayList[] lists) {
        this.lists = lists;
    }

    public TreeMap[] getAggLists() {
        return aggLists;
    }

    public void setAggLists(TreeMap[] aggLists) {
        this.aggLists = aggLists;
    }

}