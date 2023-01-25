package project.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import project.data.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Controls the 'Update Appointment' window, i.e. handles the validation and updates to existing appointments.
 */
public class UpdateAppointmentController {

    @FXML private VBox updateAppointmentPane;
    @FXML private TextField appointmentIdField;
    @FXML private TextField titleField;
    @FXML private TextField descriptionField;
    @FXML private TextField locationField;
    @FXML private ComboBox<Contact> contactComboBox;
    @FXML private TextField typeField;
    @FXML private DatePicker startField;
    @FXML private TextField startTimeField;
    @FXML private DatePicker endField;
    @FXML private TextField endTimeField;
    @FXML private ComboBox<Customer> customerComboBox;
    @FXML private ComboBox<User> userComboBox;

    private int newAppointmentId;
    private String newTitle;
    private String newDescription;
    private String newLocation;
    private int newContactId;
    private String newType;
    private LocalDate newStart;
    private LocalDate newEnd;
    private LocalTime newStartTime;
    private LocalTime newEndTime;
    private int newCustomerId;
    private int newUserId;

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("h:mm a", Locale.US);
    private static final DateTimeFormatter toStandardDateFormat = DateTimeFormatter.ofPattern("M/d/yyyy");

    /**
     * Initializes the window, sets the appointment ID, and sets up all ComboBoxes in the window.
     * <br><br>
     * -------- LAMBDA --------
     * <br><br>
     * Lambda expressions were used to set the ComboBoxes as they were a simple way to do so.
     *
     */
    public void initialize() {
        Callback<ListView<Contact>, ListCell<Contact>> factory = lv -> new ListCell<>() {
            @Override
            protected void updateItem(Contact contact, boolean empty) {
                super.updateItem(contact, empty);
                setText(empty ? "" : contact.getContactName());
            }
        };

        contactComboBox.setCellFactory(factory);
        contactComboBox.setButtonCell(factory.call(null));
        contactComboBox.setItems(Datasource.getContactList());

        Callback<ListView<Customer>, ListCell<Customer>> customerFactory = lv -> new ListCell<>() {
            @Override
            protected void updateItem(Customer customer, boolean empty) {
                super.updateItem(customer, empty);
                setText(empty ? "" : customer.getCustomerName());
            }
        };

        customerComboBox.setCellFactory(customerFactory);
        customerComboBox.setButtonCell(customerFactory.call(null));
        customerComboBox.setItems(Datasource.getCustomerList());

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

        StringConverter<LocalDate> dateConverter = new StringConverter<>() {
            @Override public String toString(LocalDate date) {
                if (date != null) {
                    return toStandardDateFormat.format(date);
                } else {
                    return "";
                }
            }

            @Override public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, toStandardDateFormat);
                } else {
                    return null;
                }
            }
        };

        startField.setConverter(dateConverter);
        endField.setConverter(dateConverter);
    }

    /**
     * Sets the fields and ComboBoxes in the form to values held by the appointment being updated.
     *
     * @param appointment - Appointment : the appointment being updated
     */
    public void setFields(Appointment appointment) {
        appointmentIdField.setText(appointment.getAppointmentId() + "");
        titleField.setText(appointment.getTitle());
        descriptionField.setText(appointment.getDescription());
        locationField.setText(appointment.getLocation());
        contactComboBox.getSelectionModel().select(Datasource.getContact(appointment.getContactId()));
        typeField.setText(appointment.getType());
        startField.setValue(appointment.getStartTime().toLocalDate());
        startTimeField.setText(dtf.format(DateConverter.convertUTCToSystem(appointment.getStartTime())));
        endField.setValue(appointment.getEndTime().toLocalDate());
        endTimeField.setText(dtf.format(DateConverter.convertUTCToSystem(appointment.getEndTime())));
        customerComboBox.getSelectionModel().select(Datasource.getCustomer(appointment.getCustomerId()));
        userComboBox.getSelectionModel().select(Datasource.getUser(appointment.getUserId()));
    }

    /**
     * Creates a new Appointment object, sets its properties to the values specified by the user (after validation), updates the previous appointment stored in the database, and finally closes the window.
     */
    public void updateAppointmentHandler() {
        if (validateForm()) {
            Appointment newAppointment = new Appointment();
            newAppointment.setAppointmentId(newAppointmentId);
            newAppointment.setTitle(newTitle);
            newAppointment.setDescription(newDescription);
            newAppointment.setLocation(newLocation);
            newAppointment.setContactId(newContactId);
            newAppointment.setType(newType);
            newAppointment.setStartTime(DateConverter.convertSystemToUTC(LocalDateTime.of(newStart, newStartTime)).toLocalDateTime());
            newAppointment.setEndTime(DateConverter.convertSystemToUTC(LocalDateTime.of(newEnd, newEndTime)).toLocalDateTime());
            newAppointment.setCustomerId(newCustomerId);
            newAppointment.setUserId(newUserId);

            Datasource.updateAppointment(newAppointment);
            closeWindowHandler();
        }
    }

    /**
     * Validates the user input; checks for inappropriate values as well as ensures that the 'time' and 'overlapping' constraints are met.
     *
     * @return boolean concerning whether all input is valid
     */
    public boolean validateForm() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (
                !titleField.getText().isEmpty() &&
                        !descriptionField.getText().isEmpty() &&
                        !locationField.getText().isEmpty() &&
                        contactComboBox.getSelectionModel().getSelectedItem() != null &&
                        !typeField.getText().isEmpty() &&
                        startField.getValue() != null &&
                        !startTimeField.getText().isEmpty() &&
                        endField.getValue() != null &&
                        !endTimeField.getText().isEmpty() &&
                        customerComboBox.getSelectionModel().getSelectedItem() != null &&
                        userComboBox.getSelectionModel().getSelectedItem() != null
        ) {
            try {
                newAppointmentId = Integer.parseInt(appointmentIdField.getText());
                newTitle = titleField.getText();
                newDescription = descriptionField.getText();
                newLocation = locationField.getText();
                newContactId = contactComboBox.getSelectionModel().getSelectedItem().getContactId();
                newType = typeField.getText();
                newStart = startField.getValue();
                newEnd = endField.getValue();
                try {
                    newStartTime = LocalTime.parse(startTimeField.getText(), dtf);
                    newEndTime = LocalTime.parse(endTimeField.getText(), dtf);
                } catch (DateTimeParseException e) {
                    alert.setContentText("Invalid time entered!");
                    alert.show();
                    return false;
                }
                newCustomerId = customerComboBox.getSelectionModel().getSelectedItem().getCustomerId();
                newUserId = userComboBox.getSelectionModel().getSelectedItem().getUserId();

                ZonedDateTime businessStartEST = ZonedDateTime.of(LocalDateTime.of(newStart, LocalTime.of(7, 59)), ZoneId.of("US/Eastern"));
                ZonedDateTime businessEndEST = ZonedDateTime.of(LocalDateTime.of(newEnd, LocalTime.of(22, 1)), ZoneId.of("US/Eastern"));
                ZonedDateTime businessStartLocal = businessStartEST.withZoneSameInstant(ZoneId.systemDefault());
                ZonedDateTime businessEndLocal = businessEndEST.withZoneSameInstant(ZoneId.systemDefault());

                if (businessStartLocal.isBefore(businessEndLocal)) {
                    if (newStartTime.isAfter(businessStartLocal.toLocalTime()) && newStartTime.isBefore(businessEndLocal.toLocalTime())) {
                        if (newEndTime.isAfter(businessStartLocal.toLocalTime()) && newEndTime.isBefore(businessEndLocal.toLocalTime())) {
                            for (Appointment appointment : Datasource.getAppointmentList()) {
                                if (appointment.getCustomerId() == newCustomerId && appointment.getAppointmentId() != newAppointmentId) {
                                    ZonedDateTime localAppointmentStartTime = DateConverter.convertUTCToSystem(appointment.getStartTime());
                                    ZonedDateTime localAppointmentEndTime = DateConverter.convertUTCToSystem(appointment.getEndTime());
                                    ZonedDateTime inputStartTime = ZonedDateTime.of(LocalDateTime.of(newStart, newStartTime), ZoneId.systemDefault());
                                    ZonedDateTime inputEndTime = ZonedDateTime.of(LocalDateTime.of(newEnd, newEndTime), ZoneId.systemDefault());
                                    if (localAppointmentStartTime.isBefore(inputEndTime) && localAppointmentStartTime.isAfter(inputStartTime)) {
                                        alert.setContentText("Times overlap with another appointment!");
                                        alert.show();
                                        return false;
                                    }
                                    if (localAppointmentEndTime.isBefore(inputEndTime) && localAppointmentEndTime.isAfter(inputStartTime)) {
                                        alert.setContentText("Times overlap with another appointment!");
                                        alert.show();
                                        return false;
                                    }
                                    if (localAppointmentEndTime.isBefore(inputEndTime) && localAppointmentStartTime.isAfter(inputStartTime)) {
                                        alert.setContentText("Times overlap with another appointment!");
                                        alert.show();
                                        return false;
                                    }
                                    if (localAppointmentEndTime.isAfter(inputEndTime) && localAppointmentStartTime.isBefore(inputStartTime)) {
                                        alert.setContentText("Times overlap with another appointment!");
                                        alert.show();
                                        return false;
                                    }
                                    if (localAppointmentStartTime.isEqual(inputStartTime) || localAppointmentEndTime.isEqual(inputEndTime)) {
                                        alert.setContentText("Times overlap with another appointment!");
                                        alert.show();
                                        return false;
                                    }
                                }
                            }
                            return true;
                        } else {
                            alert.setContentText("End time is before 8:00 AM EST or after 10:00 PM EST!");
                            alert.show();
                            return false;
                        }
                    } else {
                        alert.setContentText("Start time is before 8:00 AM EST or after 10:00 PM EST!");
                        alert.show();
                        return false;
                    }
                }
                else {
                    alert.setContentText("End time is set before start!");
                    alert.show();
                    return false;
                }
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
        Stage stage = (Stage) updateAppointmentPane.getScene().getWindow();
        stage.close();
    }
    
}
