package com.restaurant;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private Stage primaryStage;  // Store the reference to the primary stage

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;  // Initialize the primaryStage

        // Load the initial scene (main-view)
        loadMainView();
    }

    private void loadMainView() throws IOException {
        // Load the main-view.fxml
        Parent root = FXMLLoader.load(getClass().getResource("/views/main-view.fxml"));
        Scene scene = new Scene(root, 900, 600);

        // Optional: Add CSS styling
        scene.getStylesheets().add(getClass().getResource("/style/styles.css").toExternalForm());

        primaryStage.setTitle("Restaurant Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void loadClientView() {
        try {
            // Load the client-view.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/client-view.fxml"));
            Parent root = loader.load();

            // Replace the current scene with the client-view scene
            Scene clientScene = new Scene(root, 900, 600);
            clientScene.getStylesheets().add(getClass().getResource("/style/styles.css").toExternalForm());

            primaryStage.setScene(clientScene);  // Set the new scene
            primaryStage.show();  // Show the primary stage again (although this is not necessary, it's just a reminder)
        } catch (IOException e) {
            e.printStackTrace();  // Log the error for debugging
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
