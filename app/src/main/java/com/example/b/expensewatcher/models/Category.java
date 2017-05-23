package com.example.b.expensewatcher.models;

/**
 * Created by B on 24-Mar-17.
 */

public class Category {

    public int id;
    public String title;
    public String category_type;

    public Category(int id) {this.id = id; }

    public Category(String title) {this.title = title; }

    public String getTitle() {return title; }

    public String getCategory_type() {return category_type;}

}
