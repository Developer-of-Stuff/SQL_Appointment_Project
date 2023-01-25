package project.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import project.data.Appointment;
import project.data.Datasource;
import project.data.DateConverter;

import java.io.IOException;
import java.time.ZonedDateTime;

/**
 * Controls the 'Dashboard' window.
 */
public class DashboardController {

    @FXML private VBox dashboardPane;
    @FXML private Label welcomeText;
    @FXML private Label upcomingAppointments;

    /**
     * Initializes the window. Displays a message detailing any appointments the user has within the next 15 minutes of the window's loading time.
     */
    public void initialize() {
        for (Appointment appointment : Datasource.getAppointmentList()) {
            if (appointment.getUserId() == Datasource.getCurrentUser().getUserId()) {
                ZonedDateTime localAppointmentStartTime = DateConverter.convertUTCToSystem(appointment.getStartTime());
                if (localAppointmentStartTime.isAfter(ZonedDateTime.now()) && localAppointmentStartTime.isBefore(ZonedDateTime.now().plusMinutes(15))) {
                    upcomingAppointments.setText(
                            "You have an appointment in the next 15 minutes:" +
                            "\nAppointment ID: " + appointment.getAppointmentId() +
                            "\nStart Date & Time: " + DateConverter.format(localAppointmentStartTime)
                    ); return;
                }
            }
        }
        upcomingAppointments.setText("No upcoming appointments.");
    }

    /**
     * Loads the 'Customer Records' window.
     */
    public void customerRecordsAccessHandler() {
        loadScene("customerRecords.fxml");
    }

    /**
     * Loads the 'Appointments' window.
     */
    public void appointmentsAccessHandler() {
        loadScene("appointments.fxml");
    }

    /**
     * Loads the 'Reports' window.
     */
    public void generateReportsHandler() {
        loadScene("reports.fxml");
    }

    /**
     * Sets the welcome text to display the username of the current user.
     *
     * @param username - String
     */
    public void setUsername(String username) {
        welcomeText.setText("Welcome, " + username + "!");
    }

    /**
     * Helper method which loads a scene from a file path specified as an argument.
     *
     * @param fxmlFilePath - String : the file path to the scene to be loaded.
     */
    private void loadScene(String fxmlFilePath) {
        Stage dashboardStage = (Stage) dashboardPane.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(fxmlFilePath));
        try {
            dashboardStage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) {
            System.out.println("Couldn't load scene.");
            e.printStackTrace();
        }
    }
}
