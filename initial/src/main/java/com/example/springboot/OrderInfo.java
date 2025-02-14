package com.example.springboot;

//Validation
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class OrderInfo {

    //Attributes
    @DecimalMin(value="0.01", message="Price must be greater than 0")
    private double price;
    @Min(value=1, message="Amount must be greater than 0")
    private int amount;
    @Size(min=1,message="Action cannot be null")
    private String action;

    //Constructor
    public OrderInfo( double  price, int amount, String action ){
        this.price=price;
        this.amount=amount;
        this.action=action;
    }

    //Methods
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
