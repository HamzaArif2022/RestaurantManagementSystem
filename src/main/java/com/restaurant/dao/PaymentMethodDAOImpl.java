package com.restaurant.dao;

import com.restaurant.config.DBConfig;
import com.restaurant.models.PaymentMethod;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentMethodDAOImpl implements PaymentMethodDAO {
    @Override
    public List<PaymentMethod> getAllPaymentMethods() throws SQLException {
        List<PaymentMethod> paymentMethods = new ArrayList<>();
        String sql = "SELECT * FROM payment_methods"; // Adjust table name as needed

        try (Connection conn = DBConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                PaymentMethod paymentMethod = new PaymentMethod(
                        rs.getInt("id"),
                        rs.getString("label"),
                        rs.getBoolean("is_active")
                );
                paymentMethods.add(paymentMethod);
            }
        }
        return paymentMethods;
    }

    @Override
    public void addPaymentMethod(PaymentMethod paymentMethod) throws SQLException {
        String sql = "INSERT INTO payment_methods (label, is_active) VALUES (?, ?)";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, paymentMethod.getLabel());
            stmt.setBoolean(2, paymentMethod.isActive());
            stmt.executeUpdate();
        }
    }

    @Override
    public void updatePaymentMethod(PaymentMethod paymentMethod) throws SQLException {
        String sql = "UPDATE payment_methods SET label = ?, is_active = ? WHERE id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, paymentMethod.getLabel());
            stmt.setBoolean(2, paymentMethod.isActive());
            stmt.setInt(3, paymentMethod.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public List<PaymentMethod> getActivePaymentMethods() throws SQLException {
        List<PaymentMethod> paymentMethods = new ArrayList<>();
        String sql = "SELECT * FROM payment_methods WHERE is_active = TRUE"; // Adjust table name as needed

        try (Connection conn = DBConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                PaymentMethod paymentMethod = new PaymentMethod(
                        rs.getInt("id"),
                        rs.getString("label"),
                        rs.getBoolean("is_active")
                );
                paymentMethods.add(paymentMethod);
            }
        }
        return paymentMethods;
    }

    @Override
    public void deletePaymentMethod(int id) throws SQLException {
        String sql = "DELETE FROM payment_methods WHERE id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}