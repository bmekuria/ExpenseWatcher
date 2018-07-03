package com.example.b.expensewatcher.models;

import java.util.Date;

/**
 * Created by B on 16-Mar-17.
 */

public class Expense {

    public Integer $id;
    public Float amount;
    public Date timestamp;
    public String monthyear;
    public String category;
    public String details;

    public Integer getId() {return $id; }

    public void setId(Integer id) {this.$id = id; }

    public void setAmount(Float amount) { this.amount = amount;}

    public void setCategory(String category) { this.category = category; }

    public void setDetails(String details) { this.details = details; }

    public Expense()
    {}

    public Expense(Integer id) {this.$id = id; }

    public float getAmount() { return amount; }

    public Date getTimestamp() {return timestamp; }

    public String getCategory() {return category; }

    public String getDetails() { return details; }

    public Expense(Float amount, Date timestamp, String category, String details) {
        this.amount = amount;
        this.timestamp = timestamp;
        this.category = category;
        this.details = details;
    }

    public Expense(Integer id, Float amount, Date timestamp, String category, String details) {
        this.$id = id;
        this.amount = amount;
        this.timestamp = timestamp;
        this.category = category;
        this.details = details;
    }




}
