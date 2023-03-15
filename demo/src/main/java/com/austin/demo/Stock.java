package com.austin.demo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class Stock {

    @Getter @Setter
    Date date;
    @Getter @Setter
    int id;
    @Getter @Setter
    float open;
    @Getter @Setter
    float high;
    @Getter @Setter
    float low;
    @Getter @Setter
    float close;
    @Getter @Setter
    float trade;
    @Getter @Setter
    float amount;
    @Getter @Setter
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
}
