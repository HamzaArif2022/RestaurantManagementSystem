package com.restaurant.dao;

import com.restaurant.models.OrderItem;
import java.sql.SQLException;
import java.util.List;

public interface OrderItemDAO {
    void addOrderItem(OrderItem orderItem) throws SQLException;
    void updateOrderItem(OrderItem orderItem) throws SQLException;
    void deleteOrderItem(int orderItemId) throws SQLException;
    List<OrderItem> getOrderItemsByOrder(int orderId) throws SQLException;
    void updateItemStatus(int orderItemId, String status) throws SQLException;
}