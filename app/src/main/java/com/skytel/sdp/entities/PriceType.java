package com.skytel.sdp.entities;

/**
 * Created by Altanchimeg on 6/17/2016.
 */

public class PriceType {
    private int id;
    private String price;
    private String unit;
    private String days;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }
}
