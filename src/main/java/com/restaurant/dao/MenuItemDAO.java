package com.restaurant.dao;

import com.restaurant.models.MenuItem;

import java.sql.SQLException;
import java.util.List;

public interface MenuItemDAO {
    List<MenuItem> getAllMenuItems() throws SQLException;
    void addMenuItem(MenuItem item) throws SQLException;
    void updateMenuItem(MenuItem item) throws SQLException;
    void deleteMenuItem(int itemId) throws SQLException;
    double getItemPrice(int itemId) throws SQLException;


}