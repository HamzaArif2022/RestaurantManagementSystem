package com.restaurant.controllers;

import com.restaurant.dao.EmployeeDAO;
import com.restaurant.dao.EmployeeDAOImpl;
import com.restaurant.models.Employee;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EmployeeController {
    @FXML private TextField nameField;
    @FXML private TextField roleField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TableView<Employee> employeeTable;
    @FXML private TableColumn<Employee, Integer> employeeIdColumn;
    @FXML private TableColumn<Employee, String> nameColumn;
    @FXML private TableColumn<Employee, String> roleColumn;
    @FXML private TableColumn<Employee, String> emailColumn;
    @FXML private TableColumn<Employee, String> phoneColumn;
    @FXML private TableColumn<Employee, String> hireDateColumn;

    private EmployeeDAO employeeDAO;
    private ObservableList<Employee> employees;

    public void initialize() {
        employeeDAO = new EmployeeDAOImpl();
        employees = FXCollections.observableArrayList();

        // Set up the columns for the employee table
        employeeIdColumn.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        hireDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getHireDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

        refreshEmployeeTable();

        // Add listener to populate fields when an employee is selected
        employeeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFields(newSelection);
            }
        });
    }

    private void refreshEmployeeTable() {
        try {
            List<Employee> employeeList = employeeDAO.getAllEmployees();
            employees = FXCollections.observableArrayList(employeeList);
            employeeTable.setItems(employees);
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load employees: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void populateFields(Employee employee) {
        nameField.setText(employee.getName());
        roleField.setText(employee.getRole());
        emailField.setText(employee.getEmail());
        phoneField.setText(employee.getPhone());
    }

    @FXML
    private void handleAddEmployee() {
        String name = nameField.getText();
        String role = roleField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();

        Employee newEmployee = new Employee(name, role, email, phone);

        try {
            employeeDAO.addEmployee(newEmployee);
            refreshEmployeeTable();
            clearInputFields();
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to add employee: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleUpdateEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showAlert("Selection Error", "Please select an employee to update.", Alert.AlertType.ERROR);
            return;
        }

        String name = nameField.getText();
        String role = roleField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();

        selectedEmployee.setName(name);
        selectedEmployee.setRole(role);
        selectedEmployee.setEmail(email);
        selectedEmployee.setPhone(phone);

        try {
            employeeDAO.updateEmployee(selectedEmployee);
            refreshEmployeeTable();
            clearInputFields();
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to update employee: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDeleteEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showAlert("Selection Error", "Please select an employee to delete.", Alert.AlertType.ERROR);
            return;
        }

        try {
            employeeDAO.deleteEmployee(selectedEmployee.getEmployeeId());
            refreshEmployeeTable();
            clearInputFields();
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to delete employee: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void clearInputFields() {
        nameField.clear();
        roleField.clear();
        emailField.clear();
        phoneField.clear();
    }
    @FXML
    private void handleclearEmployee() {
        nameField.clear();
        roleField.clear();
        emailField.clear();
        phoneField.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}