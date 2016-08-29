package com.example.android.p10inventoryapp;

/**
 * Created by aspun_000 on 8/29/2016.
 */
public class Inventory {
    private int mId;
    private String mName;
    private int mQuantity;
    private double mPrice;

    public Inventory(String name, int quantity, double price) {
        mName = name;
        mQuantity = quantity;
        mPrice = price;
    }

    public Inventory(int id, String name, int quantity, double price) {
        mId = id;
        mName = name;
        mQuantity = quantity;
        mPrice = price;
    }

    //Getter methods...
    public int getProductId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public double getPrice() {
        return mPrice;
    }

    //Setter methods. Note that product id should be received from database addition.
    public void setId(int id) {
        mId = id;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }

    public void setPrice(double price) {
        mPrice = price;
    }

    public void productSale() {
        mQuantity -= 1;
        if (mQuantity < 0) {
            mQuantity = 0;
        }
    }

}

