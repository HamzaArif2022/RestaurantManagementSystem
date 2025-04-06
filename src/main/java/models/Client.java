package com.restaurant.models;

import java.time.LocalDate;

public class Client {
    private int clientId;
    private String name;
    private String phone;
    private String email;
    private LocalDate joinDate;
    private int loyaltyPoints;

    // Constructors
    public Client() {}

    public Client(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.joinDate = LocalDate.now();
        this.loyaltyPoints = 0;
    }

    // Getters and Setters
    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }
}