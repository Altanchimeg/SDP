package com.skytel.sdp.entities;

/**
 * Created by Altanchimeg on 7/19/2016.
 */

public class InfoNewsType {
    private int id;
    private int categoryId;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
