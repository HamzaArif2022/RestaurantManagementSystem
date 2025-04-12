package com.restaurant.controllers;

import com.restaurant.dao.ClientDAO;
import com.restaurant.dao.ClientDAOImpl;
import com.restaurant.dao.EmployeeDAO;
import com.restaurant.dao.EmployeeDAOImpl;
import com.restaurant.dao.OrderDAO;
import com.restaurant.dao.OrderDAOImpl;
import com.restaurant.models.Order;
import com.restaurant.models.Client;
import com.restaurant.models.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.List;

public class OrderController {
    @FXML private TableView<Order> orderTable;
    @FXML private TextField tableNumberField;
    @FXML private TextArea notesField;
    @FXML private ComboBox<Client> clientComboBox;
    @FXML private ComboBox<Employee> employeeComboBox;

    private OrderDAO orderDAO;
    private ClientDAO clientDAO;
    private EmployeeDAO employeeDAO;
    private ObservableList<Order> orders;

    public void initialize() {
        orderDAO = new OrderDAOImpl();
        clientDAO = new ClientDAOImpl();
        employeeDAO = new EmployeeDAOImpl();

        refreshOrderTable();
        populateClientComboBox();
        populateEmployeeComboBox();
    }

    private void refreshOrderTable() {
        try {
            List<Order> orderList = orderDAO.getAllOrders();
            orders = FXCollections.observableArrayList(orderList);
            orderTable.setItems(orders);
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load orders: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void populateClientComboBox() {
        try {
            List<Client> clients = clientDAO.getAllClients();
            clientComboBox.setItems(FXCollections.observableArrayList(clients));
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load clients: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void populateEmployeeComboBox() {
        try {
            List<Employee> employees = employeeDAO.getAllEmployees();
            employeeComboBox.setItems(FXCollections.observableArrayList(employees));
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load employees: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handlePlaceOrder() {
        Client client = clientComboBox.getValue();
        Employee employee = employeeComboBox.getValue();
        int tableNumber;

        try {
            tableNumber = Integer.parseInt(tableNumberField.getText());
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Table number must be a valid integer.", Alert.AlertType.ERROR);
            return;
        }

        Order order = new Order(client, employee, tableNumber);
        order.setNotes(notesField.getText());

        try {
            orderDAO.placeOrder(order);
            clearInputFields();
            refreshOrderTable();
        } catch (SQLException e) {
            showAlert("Order Error", "Failed to place order: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void clearInputFields() {
        tableNumberField.clear();
        notesField.clear();
        clientComboBox.setValue(null);
        employeeComboBox.setValue(null);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}