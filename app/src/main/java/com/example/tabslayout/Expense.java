package com.example.tabslayout;

public class Expense {

    String Title;
    String Description;
    String Category;
    String Price;
    String timeStamp;


    public Expense() {
    }

    public Expense(String title, String description, String category, String price, String timeStamp) {
        Title = title;
        Description = description;
        Category = category;
        Price = price;
        this.timeStamp = timeStamp;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
