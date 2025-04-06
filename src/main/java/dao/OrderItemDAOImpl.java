package com.restaurant.dao;

import com.restaurant.config.DBConfig;
import com.restaurant.models.Order;
import com.restaurant.models.OrderItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAOImpl implements com.restaurant.dao.OrderItemDAO {
    @Override
    public void addOrderItem(OrderItem orderItem) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, item_id, quantity, special_requests, item_status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, orderItem.getOrder().getOrderId());
            stmt.setInt(2, orderItem.getMenuItem().getItemId());
            stmt.setInt(3, orderItem.getQuantity());
            stmt.setString(4, orderItem.getSpecialRequests());
            stmt.setString(5, orderItem.getItemStatus());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    orderItem.setOrderItemId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public void updateOrderItem(OrderItem orderItem) throws SQLException {
        String sql = "UPDATE order_items SET quantity = ?, special_requests = ?, item_status = ? WHERE order_item_id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderItem.getQuantity());
            stmt.setString(2, orderItem.getSpecialRequests());
            stmt.setString(3, orderItem.getItemStatus());
            stmt.setInt(4, orderItem.getOrderItemId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteOrderItem(int orderItemId) throws SQLException {
        String sql = "DELETE FROM order_items WHERE order_item_id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderItemId);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<OrderItem> getOrderItemsByOrder(int orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT * FROM order_items WHERE order_id = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    items.add(mapResultSetToOrderItem(rs));
                }
            }
        }
        return items;
    }

    @Override
    public void updateItemStatus(int orderItemId, String status) throws SQLException {
        String sql = "UPDATE order_items SET item_status = ? WHERE order_item_id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, orderItemId);
            stmt.executeUpdate();
        }
    }

    private OrderItem mapResultSetToOrderItem(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setOrderItemId(rs.getInt("order_item_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setSpecialRequests(rs.getString("special_requests"));
        item.setItemStatus(rs.getString("item_status"));

        // Note: Order and MenuItem objects would need to be fetched separately
        // For simplicity, we're just setting IDs here
        com.restaurant.models.Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        item.setOrder(order);

        com.restaurant.models.MenuItem menuItem = new com.restaurant.models.MenuItem();
        menuItem.setItemId(rs.getInt("item_id"));
        item.setMenuItem(menuItem);

        return item;
    }
}