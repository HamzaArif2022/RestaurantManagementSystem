    package com.restaurant.controllers;

    import com.restaurant.dao.PaymentMethodDAO;
    import com.restaurant.dao.PaymentMethodDAOImpl;
    import com.restaurant.models.PaymentMethod;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.fxml.FXML;
    import javafx.scene.control.*;
    import javafx.scene.control.cell.PropertyValueFactory;
    import java.sql.SQLException;
    import java.util.List;

    public class PaymentMethodController {
        @FXML private TextField labelField;
        @FXML private CheckBox isActiveCheckbox;
        @FXML private TableView<PaymentMethod> paymentMethodTable;
        @FXML private TableColumn<PaymentMethod, Integer> idColumn;
        @FXML private TableColumn<PaymentMethod, String> labelColumn;
        @FXML private TableColumn<PaymentMethod, Boolean> isActiveColumn;


        private PaymentMethodDAO paymentMethodDAO;
        private ObservableList<PaymentMethod> paymentMethods;

        public void initialize() {
            paymentMethodDAO = new PaymentMethodDAOImpl();
            paymentMethods = FXCollections.observableArrayList();
            paymentMethodDAO = new PaymentMethodDAOImpl();

            // Set up the table columns
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            labelColumn.setCellValueFactory(new PropertyValueFactory<>("label"));
            isActiveColumn.setCellValueFactory(new PropertyValueFactory<>("isActive"));

            refreshPaymentMethodTable();


            // Add a listener to populate fields when a payment method is selected
            paymentMethodTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    populateFields(newSelection);
                }
            });
        }



        private void refreshPaymentMethodTable() {
            try {
                List<PaymentMethod> paymentMethodList = paymentMethodDAO.getAllPaymentMethods();
                paymentMethods = FXCollections.observableArrayList(paymentMethodList);
                paymentMethodTable.setItems(paymentMethods);
            } catch (SQLException e) {
                showAlert("Database Error", "Failed to load payment methods: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }

    private void populateFields(PaymentMethod paymentMethod) {
        labelField.setText(paymentMethod.getLabel());
        isActiveCheckbox.setSelected(paymentMethod.isActive());
    }

    @FXML
    private void handleAddPaymentMethod() {
        String label = labelField.getText();
        boolean isActive = isActiveCheckbox.isSelected();

        PaymentMethod newPaymentMethod = new PaymentMethod(0, label, isActive);
        try {
            paymentMethodDAO.addPaymentMethod(newPaymentMethod);
            refreshPaymentMethodTable();
            clearInputFields();
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to add payment method: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleUpdatePaymentMethod() {
        PaymentMethod selectedPaymentMethod = paymentMethodTable.getSelectionModel().getSelectedItem();
        if (selectedPaymentMethod == null) {
            showAlert("Selection Error", "Please select a payment method to update.", Alert.AlertType.ERROR);
            return;
        }

        selectedPaymentMethod.setLabel(labelField.getText());
        selectedPaymentMethod.setActive(isActiveCheckbox.isSelected());

        try {
            paymentMethodDAO.updatePaymentMethod(selectedPaymentMethod);
            refreshPaymentMethodTable();
            clearInputFields();
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to update payment method: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDeletePaymentMethod() {
        PaymentMethod selectedPaymentMethod = paymentMethodTable.getSelectionModel().getSelectedItem();
        if (selectedPaymentMethod == null) {
            showAlert("Selection Error", "Please select a payment method to delete.", Alert.AlertType.ERROR);
            return;
        }

        try {
            paymentMethodDAO.deletePaymentMethod(selectedPaymentMethod.getId());
            refreshPaymentMethodTable();
            clearInputFields();
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to delete payment method: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void clearInputFields() {
        labelField.clear();
        isActiveCheckbox.setSelected(false);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}