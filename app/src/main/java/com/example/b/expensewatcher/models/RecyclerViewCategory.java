package com.example.b.expensewatcher.models;

/**
 * Created by B on 28-Mar-18.
 */

public class RecyclerViewCategory {

    public String title;
    public String image;
    public String color;
    public String category_type;

    public RecyclerViewCategory(String t, String d, String c, String cat_type)
    {
        title=t;
        image=d;
        color=c;
        category_type = cat_type;
    }

    public RecyclerViewCategory(String t) {
        title = t;
    }

    public RecyclerViewCategory() {
    }
}
