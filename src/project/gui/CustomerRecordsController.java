package project.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.data.*;

import java.io.IOException;
import java.util.Optional;

/**
 * Controls the 'Customer Records' window, i.e. the display and filtering of all loaded customers, as well as granting access to CRUD functions for the MySQL database.
 */
public class CustomerRecordsController {

    @FXML private VBox customerRecordsPane;
    @FXML private TableView<Customer> customerTableView;
    @FXML private TableColumn<Customer, Integer> customerId;
    @FXML private TableColumn<Customer, String> customerName;
    @FXML private TableColumn<Customer, String> customerAddress;
    @FXML private TableColumn<Customer, String> customerPostalCode;
    @FXML private TableColumn<Customer, String> customerPhone;
    @FXML private TableColumn<Customer, String> customerCountry;
    @FXML private TableColumn<Customer, String> customerDivision;

    /**
     * Initializes the window. Sets the display for all TableColumns.
     * <br><br>
     * -------- LAMBDA --------
     * <br><br>
     * Lambda expressions were used as the country and division columns needed to display data that was not available via a PropertyValueFactory.
     *
     */
    public void initialize() {

        FilteredList<Customer> filteredCustomerList = new FilteredList<>(Datasource.getCustomerList());
        SortedList<Customer> sortedCustomerList = new SortedList<>(filteredCustomerList);
        customerTableView.setItems(sortedCustomerList);

        sortedCustomerList.comparatorProperty().bind(customerTableView.comparatorProperty());

        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        customerCountry.setCellValueFactory(customer -> {
            Country country = Datasource.getCountry(customer.getValue().getCountryId());
            if (country == null) {
                return null;
            }
            return new SimpleStringProperty(country.getCountryName());
        });
        customerDivision.setCellValueFactory(customer -> {
            Division division = Datasource.getDivision(customer.getValue().getDivisionId());
            if (division == null) {
                return null;
            }
            return new SimpleStringProperty(division.getDivisionName());
        });

//        Source for the idea of using SimpleStringProperty:
//        https://stackoverflow.com/questions/30334450/cannot-convert-from-string-to-observablevaluestring
    }

    /**
     * Opens the 'Add Customer' window.
     */
    public void addCustomerHandler() {
        Stage addCustomerStage = new Stage();
        addCustomerStage.initOwner(customerRecordsPane.getScene().getWindow());
        addCustomerStage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addCustomerRecord.fxml"));
        try {
            Scene addCustomerScene = new Scene(fxmlLoader.load());
            addCustomerStage.setScene(addCustomerScene);
            addCustomerStage.showAndWait();
        } catch (IOException e) {
            System.out.println("Couldn't load scene.");
            e.printStackTrace();
        }
    }

    /**
     * Opens the 'Update Customer' window for the selected customer.
     */
    public void updateCustomerHandler() {
        Stage updateCustomerStage = new Stage();
        updateCustomerStage.initOwner(customerRecordsPane.getScene().getWindow());
        updateCustomerStage.initModality(Modality.APPLICATION_MODAL);
        Customer customer = customerTableView.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("updateCustomerRecord.fxml"));

        if (customer != null) {
            try {
                Scene updateCustomerScene = new Scene(fxmlLoader.load());
                updateCustomerStage.setScene(updateCustomerScene);
                UpdateCustomerRecordController controller = fxmlLoader.getController();
                controller.setFields(customer);
                updateCustomerStage.showAndWait();
            } catch (IOException e) {
                System.out.println("Couldn't load scene.");
                e.printStackTrace();
            }
        }
    }

    /**
     * Deletes the selected customer from the database, pending a confirmation prompt. Also deletes all associated appointments beforehand.
     */
    public void deleteCustomerHandler() {
        Customer customer = customerTableView.getSelectionModel().getSelectedItem();
        if (customer != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this record? \nDeleting this record will also delete all appointments associated with this record.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();
                for (Appointment appointment : Datasource.getAppointmentList()) {
                    if (appointment.getCustomerId() == customer.getCustomerId()) {
                        customerAppointments.add(appointment);
                    }
                }
                for (Appointment appointment : customerAppointments) {
                    Datasource.deleteAppointment(appointment);
                }
                Datasource.deleteCustomer(customer);
            }
        }
    }

    /**
     * Changes the scene back to the dashboard.
     */
    public void backToDashboardHandler() {
        Stage dashboardStage = (Stage) customerRecordsPane.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("dashboard.fxml"));
        try {
            dashboardStage.setScene(new Scene(fxmlLoader.load()));
            DashboardController controller = fxmlLoader.getController();
            controller.setUsername(Datasource.getCurrentUser().getUsername());
        } catch (IOException e) {
            System.out.println("Couldn't load scene.");
            e.printStackTrace();
        }
    }

}
