package com.restaurant.dao;

import com.restaurant.config.DBConfig;
import com.restaurant.models.MenuItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDAOImpl implements MenuItemDAO {
    private Connection connection;

    public MenuItemDAOImpl() throws SQLException {
        this.connection = DBConfig.getConnection(); // Ensure you have a DBConfig class for database connection
    }

    @Override
    public List<MenuItem> getAllMenuItems() throws SQLException {
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT * FROM menu_items"; // Adjust table name as needed

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                MenuItem item = new MenuItem(
                        rs.getInt("item_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("price"),
                        rs.getString("category"),
                        rs.getInt("preparation_time"),
                        rs.getBoolean("is_available")
                );
                items.add(item);
            }
        }
        return items;
    }

    @Override
    public void addMenuItem(MenuItem item) throws SQLException {
        String sql = "INSERT INTO menu_items (name, description, price, category, preparation_time, is_available) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setBigDecimal(3, item.getPrice());
            stmt.setString(4, item.getCategory());
            stmt.setInt(5, item.getPreparationTime());
            stmt.setBoolean(6, item.isAvailable());
            stmt.executeUpdate();
        }
    }
    @Override
    public double getItemPrice(int itemId) throws SQLException {
        String sql = "SELECT price FROM Menu_items WHERE item_id = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("price");
            }
        }
        return 0.0; // Default value if not found
    }

    @Override
    public void updateMenuItem(MenuItem item) throws SQLException {
        String sql = "UPDATE menu_items SET name = ?, description = ?, price = ?, category = ?, preparation_time = ?, is_available = ? WHERE item_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setBigDecimal(3, item.getPrice());
            stmt.setString(4, item.getCategory());
            stmt.setInt(5, item.getPreparationTime());
            stmt.setBoolean(6, item.isAvailable());
            stmt.setInt(7, item.getItemId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteMenuItem(int itemId) throws SQLException {
        String sql = "DELETE FROM menu_items WHERE item_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, itemId);
            stmt.executeUpdate();
        }
    }
}