package com.restaurant.dao;

import com.restaurant.config.DBConfig;
import com.restaurant.models.Order;
import com.restaurant.models.OrderItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl implements com.restaurant.dao.OrderDAO {
    private final com.restaurant.dao.OrderItemDAO orderItemDAO = new com.restaurant.dao.OrderItemDAOImpl();

    @Override
    public void placeOrder(Order order) throws SQLException {
        String sql = "INSERT INTO orders (client_id, employee_id, table_number, status, notes) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, order.getClient().getClientId());
            stmt.setInt(2, order.getEmployee().getEmployeeId());
            stmt.setInt(3, order.getTableNumber());
            stmt.setString(4, order.getStatus());
            stmt.setString(5, order.getNotes());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int orderId = rs.getInt(1);
                    order.setOrderId(orderId);

                    // Save order items
                    for (OrderItem item : order.getItems()) {
                        item.setOrder(order);
                        orderItemDAO.addOrderItem(item);
                    }
                }
            }
        }
    }

    @Override
    public void updateOrderStatus(int orderId, String status) throws SQLException {
        String sql = "UPDATE orders SET status = ? WHERE order_id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, orderId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void cancelOrder(int orderId) throws SQLException {
        String sql = "UPDATE orders SET status = 'Cancelled' WHERE order_id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        }
    }

    @Override
    public Order getOrderById(int orderId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    order.setItems(orderItemDAO.getOrderItemsByOrder(orderId));
                    return order;
                }
            }
        }
        return null;
    }

    @Override
    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";

        try (Connection conn = DBConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                order.setItems(orderItemDAO.getOrderItemsByOrder(order.getOrderId()));
                orders.add(order);
            }
        }
        return orders;
    }

    @Override
    public List<Order> getOrdersByStatus(String status) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE status = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    order.setItems(orderItemDAO.getOrderItemsByOrder(order.getOrderId()));
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    @Override
    public List<Order> getOrdersByClient(int clientId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE client_id = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    order.setItems(orderItemDAO.getOrderItemsByOrder(order.getOrderId()));
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setTableNumber(rs.getInt("table_number"));
        order.setStatus(rs.getString("status"));
        order.setNotes(rs.getString("notes"));
        order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());

        // Note: Client and Employee objects would need to be fetched separately
        // For simplicity, we're just setting IDs here
        com.restaurant.models.Client client = new com.restaurant.models.Client();
        client.setClientId(rs.getInt("client_id"));
        order.setClient(client);

        com.restaurant.models.Employee employee = new com.restaurant.models.Employee();
        employee.setEmployeeId(rs.getInt("employee_id"));
        order.setEmployee(employee);

        return order;
    }
}