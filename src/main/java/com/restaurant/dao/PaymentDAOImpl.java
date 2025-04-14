package com.restaurant.dao;

import com.restaurant.config.DBConfig;
import com.restaurant.models.Payment;
import com.restaurant.models.PaymentMethod;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {
    @Override
    public void addPayment(Payment payment) throws SQLException {
        String sql = "INSERT INTO Payments (order_id, payment_method, amount, payment_time, tips) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, payment.getOrder().getOrderId()); // Assuming Order has a getOrderId method
            pstmt.setString(2, payment.getPaymentMethod());
            pstmt.setDouble(3, payment.getAmount());
            pstmt.setObject(4, payment.getPaymentTime()); // Use appropriate type for LocalDateTime
            pstmt.setDouble(5, payment.getTips());
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<Payment> getPaymentsForOrder(int orderId) throws SQLException {
        // Implementation to fetch payments related to an order (optional)
        return null; // Placeholder, implement as needed
    }
}