package com.restaurant.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    // Load Client Management View
    @FXML
    public void loadClientView() {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/client-view.fxml"));
            Parent root = loader.load();  // This is the root node of the scene

            // Create a new scene with the loaded FXML
            Scene clientScene = new Scene(root);

            // Create a new stage for the Client Management view (or reuse existing stage)
            Stage clientStage = new Stage();
            clientStage.setTitle("Client Management");  // Set a title for the window
            clientStage.setScene(clientScene);         // Set the scene
            clientStage.show();                        // Show the window
        } catch (IOException e) {
            e.printStackTrace();  // Log the error for debugging
        }
    }



    // Load Menu Management View
    @FXML
    private void loadMenuView() {
        loadView("/views/menu-view.fxml", "Menu Management");
    }

    // Load Order Management View
    @FXML
    private void loadOrderView() {
        loadView("/views/order-view.fxml", "Order Management");
    }

    // Load Employee Management View
    @FXML
    private void loadEmployeeView() {
        loadView("/views/employee-view.fxml", "Employee Management");
    }

    // Load Reports View
    @FXML
    private void loadReportsView() {
        loadView("/views/reports-view.fxml", "Reports Dashboard");
    }
    @FXML
    private void loadPaymentMethodView() {
        loadView("/views/payment_method-view.fxml", "Payment method Management");
    }

    // Open Settings Dialog
    @FXML
    private void openSettings() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/settings-dialog.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("System Settings");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            showError("Failed to load settings dialog: " + e.getMessage());
        }
    }

    // Generic View Loader
    private void loadView(String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));

            // Set reasonable default size
            stage.setMinWidth(800);
            stage.setMinHeight(600);

            stage.show();
        } catch (IOException e) {
            showError("Failed to load " + title + " view: " + e.getMessage());
        }
    }

    // Error Handling
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Exit Application
    @FXML
    private void exitApplication() {
        System.exit(0);
    }
}