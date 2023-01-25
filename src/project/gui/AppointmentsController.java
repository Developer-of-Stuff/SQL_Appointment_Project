package project.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.data.Appointment;
import project.data.Datasource;
import project.data.DateConverter;

import java.io.IOException;
import java.time.*;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Optional;

/**
 * Controls the 'Appointments' window, i.e. the display and filtering of all loaded appointments, as well as granting access to CRUD functions for the MySQL database.
 */
public class AppointmentsController {

    @FXML private VBox appointmentsPane;

    @FXML private Tab monthTab;
    @FXML private TableView<Appointment> monthlyAppointmentView;
    @FXML private ComboBox<Month> monthSelector;
    @FXML private TableColumn<Appointment, Integer> appointmentId;
    @FXML private TableColumn<Appointment, String> appointmentTitle;
    @FXML private TableColumn<Appointment, String> appointmentDescription;
    @FXML private TableColumn<Appointment, String> appointmentLocation;
    @FXML private TableColumn<Appointment, String> appointmentContact;
    @FXML private TableColumn<Appointment, String> appointmentType;
    @FXML private TableColumn<Appointment, String> appointmentStartDatetime;
    @FXML private TableColumn<Appointment, String> appointmentEndDatetime;
    @FXML private TableColumn<Appointment, String> appointmentCustomer;
    @FXML private TableColumn<Appointment, String> appointmentUser;

    @FXML private TableView<Appointment> weeklyAppointmentView;
    @FXML private TextField filterByDate;
    @FXML private TableColumn<Appointment, Integer> weekAppointmentId;
    @FXML private TableColumn<Appointment, String> weekAppointmentTitle;
    @FXML private TableColumn<Appointment, String> weekAppointmentDescription;
    @FXML private TableColumn<Appointment, String> weekAppointmentLocation;
    @FXML private TableColumn<Appointment, String> weekAppointmentContact;
    @FXML private TableColumn<Appointment, String> weekAppointmentType;
    @FXML private TableColumn<Appointment, String> weekAppointmentStartDatetime;
    @FXML private TableColumn<Appointment, String> weekAppointmentEndDatetime;
    @FXML private TableColumn<Appointment, String> weekAppointmentCustomer;
    @FXML private TableColumn<Appointment, String> weekAppointmentUser;

    private final FilteredList<Appointment> filteredAppointmentList = new FilteredList<>(Datasource.getAppointmentList());
    private final SortedList<Appointment> sortedAppointmentList = new SortedList<>(filteredAppointmentList);

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy");

    /**
     * Initializes the window, handles the display of both TableViews and the month filter's ComboBox.
     * <br><br>
     * -------- LAMBDA --------
     * <br><br>
     * Lambda expressions were used as many columns needed to display data that was not available via a PropertyValueFactory. For example: <br>
     * - appointmentContact displays the contact information taken from an Appointment object. However, the only contact information Appointment objects have is the contact ID. A contact ID could be vague or ambiguous to a user, whereas a name is much more recognizable.
     *   Hence, a function is required to set the CellValueFactory such that it displays information outside the scope of an Appointment object. Lambda expressions are the simplest way to do so.
     * <br><br>
     * Lambda expressions were also used to perform a conversion of the UTC times coming from the database to ZonedDateTimes in the user's time zone.
     *
     */
    public void initialize() {
        monthlyAppointmentView.setItems(sortedAppointmentList);
        weeklyAppointmentView.setItems(sortedAppointmentList);
        sortedAppointmentList.comparatorProperty().bind(monthlyAppointmentView.comparatorProperty());
        appointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentContact.setCellValueFactory(appointment -> {
            String contactName = Datasource.getContact(appointment.getValue().getContactId()).getContactName();
            return new SimpleStringProperty(contactName);
        });
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentStartDatetime.setCellValueFactory(appointment -> {
            ZonedDateTime zdt = DateConverter.convertUTCToSystem(appointment.getValue().getStartTime());
            return new SimpleStringProperty(DateConverter.format(zdt));
        });
        appointmentEndDatetime.setCellValueFactory(appointment -> {
            ZonedDateTime zdt = DateConverter.convertUTCToSystem(appointment.getValue().getEndTime());
            return new SimpleStringProperty(DateConverter.format(zdt));
        });
        appointmentCustomer.setCellValueFactory(appointment -> {
            String customerName = Datasource.getCustomer(appointment.getValue().getCustomerId()).getCustomerName();
            return new SimpleStringProperty(customerName);
        });
        appointmentUser.setCellValueFactory(appointment -> {
            String userName = Datasource.getUser(appointment.getValue().getUserId()).getUsername();
            return new SimpleStringProperty(userName);
        });

        weekAppointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        weekAppointmentTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        weekAppointmentDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        weekAppointmentLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        weekAppointmentContact.setCellValueFactory(appointment -> {
            String contactName = Datasource.getContact(appointment.getValue().getContactId()).getContactName();
            return new SimpleStringProperty(contactName);
        });
        weekAppointmentType.setCellValueFactory(new PropertyValueFactory<>("type"));
        weekAppointmentStartDatetime.setCellValueFactory(appointment -> {
            ZonedDateTime zdt = DateConverter.convertUTCToSystem(appointment.getValue().getStartTime());
            return new SimpleStringProperty(DateConverter.format(zdt));
        });
        weekAppointmentEndDatetime.setCellValueFactory(appointment -> {
            ZonedDateTime zdt = DateConverter.convertUTCToSystem(appointment.getValue().getEndTime());
            return new SimpleStringProperty(DateConverter.format(zdt));
        });
        weekAppointmentCustomer.setCellValueFactory(appointment -> {
            String customerName = Datasource.getCustomer(appointment.getValue().getCustomerId()).getCustomerName();
            return new SimpleStringProperty(customerName);
        });
        weekAppointmentUser.setCellValueFactory(appointment -> {
            String userName = Datasource.getUser(appointment.getValue().getUserId()).getUsername();
            return new SimpleStringProperty(userName);
        });

        ObservableList<Month> monthList = FXCollections.observableArrayList();
        Collections.addAll(monthList, Month.values());

        monthSelector.setItems(monthList);
    }

    /**
     * Opens the 'Add Appointment' window.
     */
    public void addAppointmentHandler() {
        Stage addAppointmentStage = new Stage();
        addAppointmentStage.initOwner(appointmentsPane.getScene().getWindow());
        addAppointmentStage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addAppointment.fxml"));
        try {
            Scene addAppointment = new Scene(fxmlLoader.load());
            addAppointmentStage.setScene(addAppointment);
            addAppointmentStage.showAndWait();
        } catch (IOException e) {
            System.out.println("Couldn't load scene.");
            e.printStackTrace();
        }
    }

    /**
     * Opens the 'Update Appointment' window for the selected appointment.
     */
    public void updateAppointmentHandler() {
        Stage updateAppointmentStage = new Stage();
        updateAppointmentStage.initOwner(appointmentsPane.getScene().getWindow());
        updateAppointmentStage.initModality(Modality.APPLICATION_MODAL);
        Appointment appointment;
        if (monthTab.isSelected()) {
            appointment = monthlyAppointmentView.getSelectionModel().getSelectedItem();
        }
        else {
            appointment = weeklyAppointmentView.getSelectionModel().getSelectedItem();
        }
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("updateAppointment.fxml"));

        if (appointment != null) {
            try {
                Scene updateAppointment = new Scene(fxmlLoader.load());
                updateAppointmentStage.setScene(updateAppointment);
                UpdateAppointmentController controller = fxmlLoader.getController();
                controller.setFields(appointment);
                updateAppointmentStage.showAndWait();
            } catch (IOException e) {
                System.out.println("Couldn't load scene.");
                e.printStackTrace();
            }
        }
    }

    /**
     * Deletes an appointment from the database, pending a confirmation prompt.
     */
    public void deleteAppointmentHandler() {
        Appointment appointment;
        if (monthTab.isSelected()) {
            appointment = monthlyAppointmentView.getSelectionModel().getSelectedItem();
        }
        else {
            appointment = weeklyAppointmentView.getSelectionModel().getSelectedItem();
        }

        if (appointment != null) {
            String message = "Are you sure you want to delete this appointment?\n\nAppointment ID: " + appointment.getAppointmentId() + "\nType: " + appointment.getType();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                Datasource.deleteAppointment(appointment);
            }
        }
    }

    /**
     * Handles the monthly filter for appointments.
     * <br><br>
     * -------- LAMBDA --------
     * <br><br>
     * A Lambda expression was used here in order to set the Predicate for the FilteredList in the simplest way.
     *
     */
    public void monthSelectionHandler() {
        FilteredList<Appointment> monthFilteredList = new FilteredList<>(Datasource.getAppointmentList());
        Month selectedMonth = monthSelector.getSelectionModel().getSelectedItem();
        monthFilteredList.setPredicate(appointment -> appointment.getStartTime().getMonth() == selectedMonth);
        SortedList<Appointment> monthSortedList = new SortedList<>(monthFilteredList);

        monthlyAppointmentView.setItems(monthSortedList);
        monthSortedList.comparatorProperty().bind(monthlyAppointmentView.comparatorProperty());
    }

    /**
     * Handles the weekly filter for appointments.
     * <br><br>
     * -------- LAMBDA --------
     * <br><br>
     * A Lambda expression was used here in order to set the Comparator for the SortedList in the simplest way.
     *
     */
    public void weekSelectionHandler() {
        FilteredList<Appointment> weekFilteredList = new FilteredList<>(Datasource.getAppointmentList());
        String filterDateString = filterByDate.getText();
        try {
            LocalDateTime filterDate = LocalDate.parse(filterDateString, dtf).atStartOfDay();
            weekFilteredList.setPredicate(appointment -> {
                LocalDateTime localAppointmentStartTime = DateConverter.convertUTCToSystem(appointment.getStartTime()).toLocalDateTime();
                return localAppointmentStartTime.isAfter(ChronoLocalDateTime.from(filterDate)) && localAppointmentStartTime.isBefore(ChronoLocalDateTime.from(filterDate.plusWeeks(1)));
            });
            SortedList<Appointment> weekSortedList = new SortedList<>(weekFilteredList);

            weeklyAppointmentView.setItems(weekSortedList);
            weekSortedList.comparatorProperty().bind(weeklyAppointmentView.comparatorProperty());
        } catch (DateTimeParseException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Invalid date!");
            alert.show();
        }
    }

    /**
     * Displays all appointments in both tabs by clearing out both monthly and weekly filters.
     */
    public void clearFiltersHandler() {
        monthSelector.getSelectionModel().clearSelection();
        filterByDate.setText("");
        monthlyAppointmentView.setItems(sortedAppointmentList);
        weeklyAppointmentView.setItems(sortedAppointmentList);
    }

    /**
     * Changes the scene back to the dashboard.
     */
    public void backToDashboardHandler() {
        Stage dashboardStage = (Stage) appointmentsPane.getScene().getWindow();
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
