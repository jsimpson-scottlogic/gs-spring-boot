package com.example.springboot;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Entity
@Table
public class Orders{

    @Id
    @Column
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column
    @Size(min=1,message="Account cannot be null")
    private String account;
    @Column
    @DecimalMin(value="0.01", message="Price must be greater than 0")
    private double  price;
    @Column
    @Min(value=1, message="Amount must be greater than 0")
    private int amount;
    @Column
    @Size(min=1,message="Action cannot be null")
    private String action;

    //Constructor
    public Orders(String account, double  price, int amount, String action ){
        this.account=account;
        this.price=price;
        this.amount=amount;
        this.action=action;
    }

    public Orders(){}

    //Methods
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
