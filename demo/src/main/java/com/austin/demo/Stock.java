package com.austin.demo;

import java.util.Date;

public class Stock {
    Date date;
    int id;
    float open;
    float high;
    float low;
    float close;
    float trade;
    float amount;
    String name;

    public Stock(){}
    public Stock(Date date, float open, float high, float low, float close, float trade, float amount, String name) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.trade = trade;
        this.amount = amount;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "date=" + date +
                ", id=" + id +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", trade=" + trade +
                ", amount=" + amount +
                ", name='" + name + '\'' +
                '}';
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getOpen() {
        return open;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public float getClose() {
        return close;
    }

    public void setClose(float close) {
        this.close = close;
    }

    public float getTrade() {
        return trade;
    }

    public void setTrade(float trade) {
        this.trade = trade;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
