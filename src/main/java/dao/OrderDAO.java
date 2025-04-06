package com.restaurant.dao;

import com.restaurant.models.Order;
import java.sql.SQLException;
import java.util.List;

public interface OrderDAO {
    void placeOrder(Order order) throws SQLException;
    void updateOrderStatus(int orderId, String status) throws SQLException;
    void cancelOrder(int orderId) throws SQLException;
    Order getOrderById(int orderId) throws SQLException;
    List<Order> getAllOrders() throws SQLException;
    List<Order> getOrdersByStatus(String status) throws SQLException;
    List<Order> getOrdersByClient(int clientId) throws SQLException;
}