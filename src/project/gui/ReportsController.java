package project.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import project.data.*;

import java.io.IOException;
import java.time.Month;
import java.util.Collections;
import java.util.Comparator;

/**
 * Controls the 'Reports' window, i.e. handles the creation and display of reports requested by the user.
 */
public class ReportsController {

    @FXML private VBox reportsPane;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private ComboBox<Month> monthComboBox;
    @FXML private ComboBox<Contact> contactComboBox;
    @FXML private ComboBox<User> userComboBox;
    @FXML private TextArea reportArea;

    /**
     * Initializes the window. Sets up all the appropriate ComboBoxes for display.
     * <br><br>
     * -------- LAMBDA --------
     * <br><br>
     * A Lambda expression was used to set the user and contact ComboBoxes as it was a simple way to do so.
     *
     */
    public void initialize() {
        ObservableList<Month> monthList = FXCollections.observableArrayList();
        Collections.addAll(monthList, Month.values());
        monthComboBox.setItems(monthList);

        ObservableList<String> typeList = FXCollections.observableArrayList();
        for (Appointment appointment : Datasource.getAppointmentList()) {
            boolean repeatTest = false;
            for (String type : typeList) {
                if (type.equals(appointment.getType())) {
                    repeatTest = true;
                    break;
                }
            }
            if (!repeatTest) { typeList.add(appointment.getType()); }
        }
        typeComboBox.setItems(typeList);

        Callback<ListView<Contact>, ListCell<Contact>> contactFactory = lv -> new ListCell<>() {
            @Override
            protected void updateItem(Contact contact, boolean empty) {
                super.updateItem(contact, empty);
                setText(empty ? "" : contact.getContactName());
            }
        };

        contactComboBox.setCellFactory(contactFactory);
        contactComboBox.setButtonCell(contactFactory.call(null));
        contactComboBox.setItems(Datasource.getContactList());

        Callback<ListView<User>, ListCell<User>> userFactory = lv -> new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty ? "" : user.getUsername());
            }
        };

        userComboBox.setCellFactory(userFactory);
        userComboBox.setButtonCell(userFactory.call(null));
        userComboBox.setItems(Datasource.getUserList());
    }

    /**
     * Generates a String which details the number of customer appointments of a specified type and month, and then displays that String via TextArea.
     */
    public void generateTypeMonthHandler() {
        String type = typeComboBox.getSelectionModel().getSelectedItem();
        Month month = monthComboBox.getSelectionModel().getSelectedItem();
        if (month != null) {
            int number = 0;
            for (Appointment appointment : Datasource.getAppointmentList()) {
                if (appointment.getStartTime().getMonth() == month) {
                    if (appointment.getType().equals(type)) {
                        number++;
                    }
                }
            }
            String output = "Number of customer appointments of type " + type + " and during " + month + ": " + number;
            reportArea.setText(output);
        }
    }

    /**
     * Generates a String which details all customer appointments for a particular contact, sorted by start date and time, and then displays that String via TextArea.
     */
    public void generateContactHandler() {
        ObservableList<Appointment> contactAppointments = FXCollections.observableArrayList();
        Contact contact = contactComboBox.getSelectionModel().getSelectedItem();
        StringBuilder output = new StringBuilder();
        for (Appointment appointment : Datasource.getAppointmentList()) {
            if (appointment.getContactId() == contact.getContactId()) {
                contactAppointments.add(appointment);
            }
        }
        FilteredList<Appointment> filteredList = new FilteredList<>(contactAppointments);
        SortedList<Appointment> sortedList = new SortedList<>(filteredList);
        sortedList.setComparator(Comparator.comparing(Appointment::getStartTime));

        for (Appointment appointment : sortedList) {
            output.append("Appointment ID: ").append(appointment.getAppointmentId()).append(", Title: ").append(appointment.getTitle()).append(", Type: ");
            output.append(appointment.getType()).append(", Description: ").append(appointment.getDescription()).append(", Start Date & Time: ");
            output.append(DateConverter.format(DateConverter.convertUTCToSystem(appointment.getStartTime()))).append(", End Date & Time: ");
            output.append(DateConverter.format(DateConverter.convertUTCToSystem(appointment.getEndTime()))).append(", Customer ID: ").append(appointment.getCustomerId());
            output.append("\n");
        }

        reportArea.setText(output.toString());
    }

    /**
     * Generates a String which details all customer appointments created by a particular user, sorted by start date and time, and then displays that String via TextArea.
     */
    public void generateUserHandler() {
        ObservableList<Appointment> userAppointments = FXCollections.observableArrayList();
        User user = userComboBox.getSelectionModel().getSelectedItem();
        StringBuilder output = new StringBuilder();
        for (Appointment appointment : Datasource.getAppointmentList()) {
            if (appointment.getUserId() == user.getUserId()) {
                userAppointments.add(appointment);
            }
        }
        FilteredList<Appointment> filteredList = new FilteredList<>(userAppointments);
        SortedList<Appointment> sortedList = new SortedList<>(filteredList);
        sortedList.setComparator(Comparator.comparing(Appointment::getStartTime));

        for (Appointment appointment : sortedList) {
            output.append("Appointment ID: ").append(appointment.getAppointmentId()).append(", Title: ").append(appointment.getTitle()).append(", Type: ");
            output.append(appointment.getType()).append(", Description: ").append(appointment.getDescription()).append(", Start Date & Time: ");
            output.append(DateConverter.format(DateConverter.convertUTCToSystem(appointment.getStartTime()))).append(", End Date & Time: ");
            output.append(DateConverter.format(DateConverter.convertUTCToSystem(appointment.getEndTime()))).append(", Customer ID: ").append(appointment.getCustomerId());
            output.append("\n");
        }

        reportArea.setText(output.toString());
    }

    /**
     * Changes the scene back to the dashboard.
     */
    public void backToDashboardHandler() {
        Stage dashboardStage = (Stage) reportsPane.getScene().getWindow();
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
