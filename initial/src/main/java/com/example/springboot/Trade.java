package com.example.springboot;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//Validation
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Entity
@Table
public class Trade {

    //Attributes
    @Id
    @Column
    @Size(min=1,message="Account cannot be null")
    String account1;
    @Column
    @Size(min=1,message="Account cannot be null")
    String account2;
    @Column
    @Min(value=1, message="Amount must be greater than 0")
    int quantity;
    @Column
    @DecimalMin(value="0.01", message="Price must be greater than 0")
    double price;
    @Column
    @Size(min=1,message="Action cannot be null")
    String action;

    //Constructor
    public Trade(String account1, String account2, double  price, int quantity, String action ){
        this.account1=account1;
        this.account2=account2;
        this.price=price;
        this.quantity=quantity;
        this.action=action;
    }

    public Trade(){}

    //Methods
    public String getAccount1() {
        return account1;
    }

    public void setAccount1(String account1) {
        this.account1 = account1;
    }

    public String getAccount2() {
        return account2;
    }

    public void setAccount2(String account2) {
        this.account2 = account2;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
