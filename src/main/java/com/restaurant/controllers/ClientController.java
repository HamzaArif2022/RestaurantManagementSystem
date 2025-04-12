package com.restaurant.controllers;

import com.restaurant.dao.ClientDAO;
import com.restaurant.dao.ClientDAOImpl;
import com.restaurant.models.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ClientController {
    @FXML private TableView<Client> clientTable;

    @FXML private TextField searchField;
    @FXML
    private TextField nameField; // Assuming you have this field
    @FXML
    private TextField phoneField; // Assuming you have this field
    @FXML
    private TextField emailField; // Assuming you have this field
    @FXML
    private TextField loyaltyPointsField; // Assuming you have this field

    private ClientDAO clientDAO;
    private ObservableList<Client> clients;

    public void initialize() {
        System.out.println("Initializing ClientController...");
        clientDAO = new ClientDAOImpl();
        refreshClientTable();

        clientTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateFields(newValue);
            }
        });
    }


    private void refreshClientTable() {
        try {
            List<Client> clientList = clientDAO.getAllClients();
            clients = FXCollections.observableArrayList(clientList);
            clientTable.setItems(clients);

            // Log to check if data is being loaded
            System.out.println("Loaded " + clientList.size() + " clients.");
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load clients: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    @FXML
    private void handleAddClient() {
        // Open dialog to add new client
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Client");
        dialog.setHeaderText("Enter client details");

        // Add form fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        TextField phoneField = new TextField();
        TextField emailField = new TextField();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Phone:"), 0, 1);
        grid.add(phoneField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(response -> {
            try {
                Client client = new Client();
                client.setName(nameField.getText());
                client.setPhone(phoneField.getText());
                client.setEmail(emailField.getText());
                client.setJoinDate(LocalDate.now());

                clientDAO.addClient(client);
                refreshClientTable();
            } catch (SQLException e) {
                showAlert("Error", "Failed to add client: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    @FXML
    private void handleEditClient() {
        Client selected = clientTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a client to edit", Alert.AlertType.WARNING);
            return;
        }

        // Update the selected client with values from the input fields
        selected.setName(nameField.getText());
        selected.setPhone(phoneField.getText());
        selected.setEmail(emailField.getText());

        try {
            selected.setLoyaltyPoints(Integer.parseInt(loyaltyPointsField.getText())); // Ensure this is a valid integer
            clientDAO.updateClient(selected); // Update the client in the database
            refreshClientTable(); // Refresh the client list to reflect changes
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Loyalty points must be a valid number.", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            showAlert("Update Failed", "Failed to update client: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDeleteClient() {
        Client selected = clientTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a client to delete", Alert.AlertType.WARNING);
            return;
        }

        try {
            clientDAO.deleteClient(selected.getClientId());
            refreshClientTable();
        } catch (SQLException e) {
            showAlert("Error", "Failed to delete client: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText();
        if (keyword.isEmpty()) {
            refreshClientTable();
            return;
        }

        try {
            List<Client> results = clientDAO.searchClients(keyword);
            clients = FXCollections.observableArrayList(results);
            clientTable.setItems(clients);
        } catch (SQLException e) {
            showAlert("Search Error", "Failed to search clients: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void populateFields(Client client) {
        nameField.setText(client.getName());
        phoneField.setText(client.getPhone());
        emailField.setText(client.getEmail());
        loyaltyPointsField.setText(String.valueOf(client.getLoyaltyPoints()));
    }
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}