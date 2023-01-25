package project.gui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import project.data.Country;
import project.data.Customer;
import project.data.Datasource;
import project.data.Division;

import java.util.Hashtable;

/**
 * Controls the 'Update Customer' window, i.e. handles the display, validation, and updates to existing customers.
 */
public class UpdateCustomerRecordController {

    @FXML private VBox updateCustomerPane;
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private TextField postalCodeField;
    @FXML private TextField phoneField;
    @FXML private ComboBox<Country> countryField;
    @FXML private ComboBox<Division> divisionField;

    private int newId;
    private String newName;
    private String newAddress;
    private String newPostalCode;
    private String newPhone;
    private int newCountry;
    private int newDivision;

    /**
     * Initializes the window, sets the customer ID, and sets up the country ComboBox for display.
     * <br><br>
     * -------- LAMBDA --------
     * <br><br>
     * A Lambda expression was used to set the country ComboBox as it was a simple way to do so.
     *
     */
    public void initialize() {
        Callback<ListView<Country>, ListCell<Country>> factory = lv -> new ListCell<>() {
            @Override
            protected void updateItem(Country country, boolean empty) {
                super.updateItem(country, empty);
                setText(empty ? "" : country.getCountryName());
            }
        };

        countryField.setCellFactory(factory);
        countryField.setButtonCell(factory.call(null));
        countryField.setItems(Datasource.getCountryList());
        countryFieldHandler();
    }

    /**
     * Handles the display of the division ComboBox once a country has been selected by the user.
     * <br><br>
     * -------- LAMBDA --------
     * <br><br>
     * A Lambda expression was used to set the division ComboBox as it was a simple way to do so.
     *
     */
    public void countryFieldHandler() {
        if (countryField.getSelectionModel().getSelectedItem() != null) {
            Country country = countryField.getSelectionModel().getSelectedItem();
            Hashtable<Integer, ObservableList<Division>> listings = Datasource.getDivisionListings();

            divisionField.setDisable(listings.get(country.getCountryId()) == null);

            Callback<ListView<Division>, ListCell<Division>> factory = lv -> new ListCell<>() {
                @Override
                protected void updateItem(Division division, boolean empty) {
                    super.updateItem(division, empty);
                    setText(empty ? "" : division.getDivisionName());
                }
            };

            divisionField.setCellFactory(factory);
            divisionField.setButtonCell(factory.call(null));
            divisionField.setItems(listings.get(country.getCountryId()));
        }
    }

    /**
     * Sets the fields and ComboBoxes in the form to values held by the customer being updated.
     *
     * @param customer - Customer : the customer being updated
     */
    public void setFields(Customer customer) {
        idField.setText(customer.getCustomerId() + "");
        nameField.setText(customer.getCustomerName());
        addressField.setText(customer.getAddress());
        postalCodeField.setText(customer.getPostalCode());
        phoneField.setText(customer.getPhone());
        countryField.getSelectionModel().select(Datasource.getCountry(customer.getCountryId()));
        countryFieldHandler();
        divisionField.getSelectionModel().select(Datasource.getDivision(customer.getDivisionId()));
    }

    /**
     * Creates a new Customer object, sets its properties to the values specified by the user (after validation), updates the previous customer stored in the database, and finally closes the window.
     */
    public void updateCustomerButtonHandler() {
        if (validateForm()) {
            Customer newCustomer = new Customer();
            newCustomer.setCustomerId(newId);
            newCustomer.setCustomerName(newName);
            newCustomer.setAddress(newAddress);
            newCustomer.setPostalCode(newPostalCode);
            newCustomer.setPhone(newPhone);
            newCustomer.setCountryId(newCountry);
            newCustomer.setDivisionId(newDivision);

            Datasource.updateCustomer(newCustomer);
            closeWindowHandler();
        }
    }

    /**
     * Validates the user input.
     *
     * @return boolean concerning whether all input is valid
     */
    public boolean validateForm() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (
                !nameField.getText().isEmpty() &&
                        !addressField.getText().isEmpty() &&
                        !postalCodeField.getText().isEmpty() &&
                        !phoneField.getText().isEmpty() &&
                        countryField.getSelectionModel().getSelectedItem() != null
        ) {
            try {
                newId = Integer.parseInt(idField.getText());
                newName = nameField.getText();
                newAddress = addressField.getText();
                newPostalCode = postalCodeField.getText();
                newPhone = phoneField.getText();
                newCountry = countryField.getSelectionModel().getSelectedItem().getCountryId();
                if (divisionField.getSelectionModel().getSelectedItem() == null) {
                    if (Datasource.getDivisionListings().get(newCountry) != null) {
                        alert.setContentText("Must input division!");
                        alert.show();
                        return false;
                    }
                }
                else {
                    newDivision = divisionField.getSelectionModel().getSelectedItem().getDivisionId();
                }
                return true;

            } catch (NumberFormatException nfe) {
                alert.setContentText("One or more fields are filled with invalid data!");
                alert.show();
                return false;
            }
        }

        else {
            alert.setContentText("One or more fields are empty!");
            alert.show();
            return false;
        }
    }

    /**
     * Closes the window.
     */
    public void closeWindowHandler() {
        Stage stage = (Stage) updateCustomerPane.getScene().getWindow();
        stage.close();
    }

}
