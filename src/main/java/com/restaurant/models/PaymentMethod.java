package com.restaurant.models;

public class PaymentMethod {
    private int id;
    private String label;
    private boolean isActive;

    public PaymentMethod(int id, String label, boolean isActive) {
        this.id = id;
        this.label = label;
        this.isActive = isActive;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
    @Override
    public String toString() {
        return label; // This will display the label in the ComboBox
    }
}