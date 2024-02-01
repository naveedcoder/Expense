package com.example.tabslayout;

public class AddExpense {

    String TimeStamp;
    String Description;
    String Price;


    public AddExpense() {
    }

    public AddExpense(String timeStamp, String description, String price) {
        TimeStamp = timeStamp;
        Description = description;
        Price = price;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
