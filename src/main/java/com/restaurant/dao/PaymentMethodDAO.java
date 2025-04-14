package com.restaurant.dao;

import com.restaurant.models.PaymentMethod;
import java.sql.SQLException;
import java.util.List;

public interface PaymentMethodDAO {
    List<PaymentMethod> getAllPaymentMethods() throws SQLException;
    void addPaymentMethod(PaymentMethod paymentMethod) throws SQLException;
    void updatePaymentMethod(PaymentMethod paymentMethod) throws SQLException;
    void deletePaymentMethod(int id) throws SQLException;
    List<PaymentMethod> getActivePaymentMethods() throws SQLException;
}