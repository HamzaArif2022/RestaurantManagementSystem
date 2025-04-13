package com.restaurant.controllers;

import com.restaurant.dao.*;
import com.restaurant.models.*;
import com.restaurant.models.MenuItem;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrderController {
    @FXML
    private TextField paymentAmountField;
    @FXML
    private ComboBox<String> paymentMethodComboBox;
    @FXML
    private TextField tipsField; // Optional for tips

    @FXML private TableView<Order> orderTable;
    @FXML private TableColumn<Order, Integer> orderIdColumn;
    @FXML private TableColumn<Order, String> clientColumn;
    @FXML private TableColumn<Order, String> employeeColumn;
    @FXML private TableColumn<Order, Integer> tableNumberColumn;
    @FXML private TableColumn<Order, String> statusColumn;
    @FXML private TableColumn<Order, String> orderDateColumn;
    @FXML private TableColumn<Order, Double> totalAmountColumn; // New column for total amount

    @FXML private TableView<OrderItem> orderItemsTable;
    @FXML private TableColumn<OrderItem, String> itemNameColumn;
    @FXML private TableColumn<OrderItem, Integer> itemQuantityColumn;

    @FXML private TextField tableNumberField;
    @FXML private TextArea notesField;
    @FXML private ComboBox<Client> clientComboBox;
    @FXML private ComboBox<Employee> employeeComboBox;
    @FXML private ComboBox<MenuItem> menuItemComboBox; // ComboBox for selecting MenuItem
    @FXML private TextField itemQuantityField;

    private OrderDAO orderDAO;
    private ClientDAO clientDAO;
    private EmployeeDAO employeeDAO;
    private MenuItemDAO menuItemDAO; // DAO for MenuItems
    private ObservableList<Order> orders;
    private ObservableList<OrderItem> orderItems;

    public void initialize() throws SQLException {
        orderDAO = new OrderDAOImpl();
        clientDAO = new ClientDAOImpl();
        employeeDAO = new EmployeeDAOImpl();
        menuItemDAO = new MenuItemDAOImpl(); // Initialize the MenuItem DAO
        orders = FXCollections.observableArrayList();
        orderItems = FXCollections.observableArrayList();

        // Set up the columns for orders
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        clientColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getClient().getClientId())));
        employeeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getEmployee().getEmployeeId())));
        tableNumberColumn.setCellValueFactory(new PropertyValueFactory<>("tableNumber"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        orderDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        // Set up the columns for order items
        itemNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMenuItem().getName())); // Adjust as necessary
        itemQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Populate ComboBoxes
        refreshOrderTable();
        populateClientComboBox();
        populateEmployeeComboBox();
        populateMenuItemComboBox(); // Populate the MenuItem ComboBox

        orderItemsTable.setItems(orderItems); // Bind observable list to the TableView
    }

    private void refreshOrderTable() {
        try {

            List<Order> orderList = orderDAO.getAllOrders(); // Fetch all orders from the database
            orders = FXCollections.observableArrayList(orderList); // Convert to ObservableList

            orderTable.setItems(orders); // Set the items in the TableView
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load orders: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void populateClientComboBox() {
        try {
            List<Client> clients = clientDAO.getAllClients(); // Fetch all clients
            clientComboBox.setItems(FXCollections.observableArrayList(clients)); // Populate ComboBox
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load clients: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDeleteOrder() {
        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showAlert("Selection Error", "Please select an order to delete.", Alert.AlertType.ERROR);
            return;
        }

        try {
            orderDAO.deleteOrder(selectedOrder.getOrderId()); // Assuming you have a deleteOrder method in your DAO
            refreshOrderTable(); // Refresh the order table
        } catch (SQLException e) {
            showAlert("Delete Error", "Failed to delete order: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleChangeOrderStatus() {
        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showAlert("Selection Error", "Please select an order to change its status.", Alert.AlertType.ERROR);
            return;
        }

        TextInputDialog dialog = new TextInputDialog(selectedOrder.getStatus());
        dialog.setTitle("Change Order Status");
        dialog.setHeaderText("Change Status for Order ID: " + selectedOrder.getOrderId());
        dialog.setContentText("New Status:");

        String newStatus = dialog.showAndWait().orElse(null);
        if (newStatus != null && !newStatus.trim().isEmpty()) {
            try {
                selectedOrder.setStatus(newStatus); // Assuming you have a setStatus method in your Order class
                orderDAO.updateOrder(selectedOrder); // Assuming you have an updateOrder method in your DAO
                refreshOrderTable(); // Refresh the order table
            } catch (SQLException e) {
                showAlert("Update Error", "Failed to update order status: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void populateEmployeeComboBox() {
        try {
            List<Employee> employees = employeeDAO.getAllEmployees(); // Fetch all employees
            employeeComboBox.setItems(FXCollections.observableArrayList(employees)); // Populate ComboBox
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load employees: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void populateMenuItemComboBox() {
        try {
            List<MenuItem> menuItems = menuItemDAO.getAllMenuItems(); // Fetch all menu items
            menuItemComboBox.setItems(FXCollections.observableArrayList(menuItems)); // Populate ComboBox
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load menu items: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleAddItem() {
        MenuItem selectedItem = menuItemComboBox.getValue(); // Get selected MenuItem
        int quantity;

        if (selectedItem == null) {
            showAlert("Selection Error", "Please select a menu item.", Alert.AlertType.ERROR);
            return;
        }

        try {
            quantity = Integer.parseInt(itemQuantityField.getText());
            if (quantity <= 0) {
                throw new NumberFormatException("Quantity must be greater than zero.");
            }
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter a valid quantity.", Alert.AlertType.ERROR);
            return;
        }

        // Create a new OrderItem with the selected MenuItem
        OrderItem newItem = new OrderItem(null, selectedItem, quantity);
        orderItems.add(newItem);

        // Clear input fields
        itemQuantityField.clear();
        menuItemComboBox.setValue(null); // Clear the selected item

        // Refresh the items table
        orderItemsTable.setItems(orderItems);
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
        order.setItems(new ArrayList<>(orderItems)); // Set items for the order

        try {
            // Calculate total amount before placing the order
            double totalAmount = order.calculateTotalAmount(menuItemDAO);

            order.setTotalAmount(totalAmount); // Ensure you have a setter for totalAmount
            orderDAO.placeOrder(order);
            clearInputFields();
            refreshOrderTable(); // Refresh the table after placing the order
            orderItems.clear(); // Clear items after placing the order
        } catch (SQLException e) {
            showAlert("Order Error", "Failed to place order: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void clearInputFields() {
        tableNumberField.clear();
        notesField.clear();
        clientComboBox.setValue(null);
        employeeComboBox.setValue(null);
        itemQuantityField.clear();
        menuItemComboBox.setValue(null); // Clear the selected item
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleAddPayment() {
        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showAlert("Selection Error", "Please select an order to add a payment.", Alert.AlertType.ERROR);
            return;
        }

        String paymentMethod = paymentMethodComboBox.getValue();
        double amount;
        double tips = 0;

        try {
            amount = Double.parseDouble(paymentAmountField.getText());
            if (amount <= 0) {
                throw new NumberFormatException("Amount must be greater than zero.");
            }
            if (!tipsField.getText().isEmpty()) {
                tips = Double.parseDouble(tipsField.getText());
            }
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid amounts.", Alert.AlertType.ERROR);
            return;
        }

        Payment payment = new Payment(selectedOrder, amount, paymentMethod);
        payment.setTips(tips); // Set tips if provided

        try {
            PaymentDAO paymentDAO = new PaymentDAOImpl();
            paymentDAO.addPayment(payment);
            showAlert("Success", "Payment added successfully.", Alert.AlertType.INFORMATION);
            paymentAmountField.clear();
            paymentMethodComboBox.setValue(null);
            tipsField.clear();
        } catch (SQLException e) {
            showAlert("Payment Error", "Failed to add payment: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}