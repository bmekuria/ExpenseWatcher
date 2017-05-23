package com.example.b.expensewatcher.models;

/**
 * Created by B on 19-May-17.
 */

public class CategoryPerExpense {

    public String category_title;
    public float total_expenses_per_category;
    public float share_expenses_per_category;

    public String getTitle() {return category_title; }

    public float getTotal_expenses_per_category() {return total_expenses_per_category;}

    public float getShare_expenses_per_category() {return share_expenses_per_category;}

}
