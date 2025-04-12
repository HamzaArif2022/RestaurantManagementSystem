package com.restaurant.models;

public class OrderItem {
    private int orderItemId;
    private Order order;
    private MenuItem menuItem;
    private int quantity;
    private String specialRequests;
    private String itemStatus;

    // Constructors
    public OrderItem() {}

    public OrderItem(Order order, MenuItem menuItem, int quantity) {
        this.order = order;
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.itemStatus = "Waiting";
    }

    // Getters and Setters
    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }
}