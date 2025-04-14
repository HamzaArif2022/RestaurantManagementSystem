package com.restaurant.dao;

import com.restaurant.models.Payment;
import java.sql.SQLException;
import java.util.List;

public interface PaymentDAO {
    void addPayment(Payment payment) throws SQLException;
    List<Payment> getPaymentsForOrder(int orderId) throws SQLException; // Optional

}