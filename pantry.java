package com.example.myapplication1.db;

import com.example.myapplication1.MainActivity;

public class pantry {
    private int id;
    private String name;
    private double quantity;
    private String unit;
    private String expiryD;

    public pantry() {
        //initial constructor
    }
    public pantry(int id, String name, double quantity, String unit, String expiryD){

        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryD = expiryD;
    }
    public pantry(String name, double quantity, String unit, String expiryD){
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryD = expiryD;

    }

    public int getId(){
        return  id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getName(){
        return  name;
    }
    public void setName(String name){
        this.name = name;
    }
    public double getQuantity(){
        return  quantity;
    }
    public void setQuantity(double quantity){
        this.quantity = quantity;
    }
    public String getUnit(){
        return  unit;
    }
    public void setUnit(String unit){
        this.unit = unit;
    }
    public String getExpiryD(){
        return  expiryD;
    }
    public void setExpiryD(String expiryD){
        this.expiryD = expiryD;
    }
}
