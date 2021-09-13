package com.example.springboot;

public class Order{
    String account;
    double  price;
    int amount;
    String action;

    public Order(String account, double  price, int amount, String action ){
        this.account=account;
        this.price=price;
        this.amount=amount;
        this.action=action;
    }

}
