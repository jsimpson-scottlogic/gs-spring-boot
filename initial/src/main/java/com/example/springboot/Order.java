package com.example.springboot;

public class Order{
    private String account;
    private double  price;
    private int amount;
    private String action;

    public Order(String account, double  price, int amount, String action ){
        this.account=account;
        this.price=price;
        this.amount=amount;
        this.action=action;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
