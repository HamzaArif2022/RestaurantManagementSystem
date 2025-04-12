package com.restaurant.models;

import java.math.BigDecimal;


public class MenuItem {
    private int itemId;
    private String name;
    private String description;
    private BigDecimal price; // Use BigDecimal for precise currency representation
    private String category;
    private int preparationTime; // Preparation time in minutes
    private boolean isAvailable;

    // Default constructor
    public MenuItem() {}


    // Constructor for creating a new menu item
    public MenuItem(String name, String description, BigDecimal price, String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.isAvailable = true; // Default to available
    }

    // Constructor for loading from the database
    public MenuItem(int itemId, String name, String description, BigDecimal price, String category, int preparationTime, boolean isAvailable) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.preparationTime = preparationTime;
        this.isAvailable = isAvailable;
    }

    // Getters and Setters
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}