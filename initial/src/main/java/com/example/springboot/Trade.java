package com.example.springboot;

public class Trade {
    String account1;
    String account2;
    int quantity;
    double price;
    String action;

    public Trade(String account1, String account2, double  price, int quantity, String action ){
        this.account1=account1;
        this.account2=account2;
        this.price=price;
        this.quantity=quantity;
        this.action=action;
    }
}
