package com.restaurant.controllers;

import com.restaurant.dao.MenuItemDAO;
import com.restaurant.dao.MenuItemDAOImpl;
import com.restaurant.models.MenuItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class MenuItemController {
    @FXML private TableView<MenuItem> menuTable;
    @FXML private TextField nameField;
    @FXML private TextField descriptionField;
    @FXML private TextField priceField;
    @FXML private TextField categoryField;
    @FXML private TextField preparationTimeField;
    @FXML private CheckBox availabilityCheckbox;
    @FXML private TextField searchField; // For searching items

    private MenuItemDAO menuItemDAO;
    private ObservableList<MenuItem> menuItems;

    public void initialize() throws SQLException {
        menuItemDAO = new MenuItemDAOImpl();
        refreshMenuTable();

        // Add listener for selection changes
        menuTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            handleTableSelection();
        });
    }

    private void refreshMenuTable() {
        try {
            List<MenuItem> itemList = menuItemDAO.getAllMenuItems();
            menuItems = FXCollections.observableArrayList(itemList);
            menuTable.setItems(menuItems);
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load menu items: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleAddMenuItem() {
        String name = nameField.getText();
        String description = descriptionField.getText();
        BigDecimal price;
        String category = categoryField.getText();
        int preparationTime;

        // Validate price input
        String priceInput = priceField.getText();
        if (priceInput == null || priceInput.trim().isEmpty()) {
            showAlert("Input Error", "Price must not be empty.", Alert.AlertType.ERROR);
            return;
        }
        try {
            price = new BigDecimal(priceInput);
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Price must be a valid number.", Alert.AlertType.ERROR);
            return;
        }

        // Validate preparation time input
        String preparationTimeInput = preparationTimeField.getText();
        if (preparationTimeInput == null || preparationTimeInput.trim().isEmpty()) {
            showAlert("Input Error", "Preparation time must not be empty.", Alert.AlertType.ERROR);
            return;
        }
        try {
            preparationTime = Integer.parseInt(preparationTimeInput);
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Preparation time must be a valid integer.", Alert.AlertType.ERROR);
            return;
        }

        MenuItem item = new MenuItem(name, description, price, category);
        item.setPreparationTime(preparationTime);
        item.setAvailable(availabilityCheckbox.isSelected());

        try {
            menuItemDAO.addMenuItem(item);
            clearInputFields(); // Clear fields after adding
            refreshMenuTable();
        } catch (SQLException e) {
            showAlert("Error", "Failed to add menu item: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleEditMenuItem() {
        MenuItem selected = menuTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a menu item to edit", Alert.AlertType.WARNING);
            return;
        }

        // Populate input fields with the selected item's details
        nameField.setText(selected.getName());
        descriptionField.setText(selected.getDescription());
        priceField.setText(selected.getPrice().toString());
        categoryField.setText(selected.getCategory());
        preparationTimeField.setText(String.valueOf(selected.getPreparationTime()));
        availabilityCheckbox.setSelected(selected.isAvailable());
    }

    // Add this method to handle item selection and populate fields
    @FXML
    private void handleTableSelection() {
        MenuItem selected = menuTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Populate fields with selected item
            nameField.setText(selected.getName());
            descriptionField.setText(selected.getDescription());
            priceField.setText(selected.getPrice().toString());
            categoryField.setText(selected.getCategory());
            preparationTimeField.setText(String.valueOf(selected.getPreparationTime()));
            availabilityCheckbox.setSelected(selected.isAvailable());
        }
    }


    @FXML
    private void handleDeleteMenuItem() {
        MenuItem selected = menuTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a menu item to delete", Alert.AlertType.WARNING);
            return;
        }

        try {
            menuItemDAO.deleteMenuItem(selected.getItemId());
            refreshMenuTable();
        } catch (SQLException e) {
            showAlert("Error", "Failed to delete menu item: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        ObservableList<MenuItem> filteredItems = FXCollections.observableArrayList();

        for (MenuItem item : menuItems) {
            if (item.getName().toLowerCase().contains(searchText) ||
                    item.getDescription().toLowerCase().contains(searchText)) {
                filteredItems.add(item);
            }
        }

        menuTable.setItems(filteredItems);
    }

    private void clearInputFields() {
        nameField.clear();
        descriptionField.clear();
        priceField.clear();
        categoryField.clear();
        preparationTimeField.clear();
        availabilityCheckbox.setSelected(false);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}