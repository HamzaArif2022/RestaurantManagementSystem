package com.restaurant.models;

import java.time.LocalDateTime;
import com.restaurant.models.Order;
import com.restaurant.models.OrderItem;
import com.restaurant.models.Employee;

import com.restaurant.models.Client;

import java.util.List;

public class Order {
    private int orderId;
    private Client client;
    private Employee employee;
    private int tableNumber;
    private LocalDateTime orderDate;
    private String status;
    private String notes;
    private List<OrderItem> items;

    // Constructors
    public Order() {}

    public Order(Client client, Employee employee, int tableNumber) {
        this.client = client;
        this.employee = employee;
        this.tableNumber = tableNumber;
        this.orderDate = LocalDateTime.now();
        this.status = "Pending";
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}