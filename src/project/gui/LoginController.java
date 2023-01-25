package project.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.data.Datasource;
import project.data.DateConverter;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;

/**
 * Controls the login window, i.e. handles the login validation of users, the language display, as well as the tracking of login activity. All calls to load local ObservableLists with information from the MySQL Database are also made from here.
 */
public class LoginController {

    @FXML private HBox loginPane;
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Label titleLabel;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private Label locationLabel;
    @FXML private Label locationLabelText;
    @FXML private Button loginButton;

    private final Locale locale = Locale.getDefault();

    /**
     * Initializes the window and loads the user list for validation.
     */
    public void initialize() {
        setLocationLabel();
        setLanguage();
        Datasource.loadUsers();
    }

    /**
     * Handles the login of users. If successful, all pertinent tables are loaded from the MySQL database, the successful attempt is logged in the 'login_activity.txt' file, the dashboard window is created and displayed, and the login window closes.
     * If unsuccessful, the failed attempt is still logged, and an error window is created in either English or French, depending on the user's locale.
     */
    public void loginButtonHandler() {
        try (FileWriter fileWriter = new FileWriter("login_activity.txt", true);) {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String fileOutput = "";
            if (Datasource.loginUser(username, password)) {
                Stage dashboardStage = new Stage();
                dashboardStage.setResizable(false);
                dashboardStage.initModality(Modality.APPLICATION_MODAL);
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("dashboard.fxml"));
                fileOutput += "Successful login: \nUsername: " + username + "\nTimestamp: " + DateConverter.format(DateConverter.convertSystemToUTC(LocalDateTime.now())) + " (UTC)\n\n";
                try {
                    Datasource.loadCountryList();
                    Datasource.loadDivisionListings();
                    Datasource.loadCustomerList();
                    Datasource.loadAppointmentList();
                    Datasource.loadContactList();

                    Scene dashboardScene = new Scene(fxmlLoader.load());
                    DashboardController dbController = fxmlLoader.getController();
                    dbController.setUsername(username);
                    dashboardStage.setScene(dashboardScene);
                    Stage parentStage = (Stage) loginPane.getScene().getWindow();
                    parentStage.close();
                    dashboardStage.showAndWait();
                } catch (IOException e) {
                    System.out.println("Couldn't load scene.");
                    e.printStackTrace();
                }
            }
            else {
                fileOutput += "Failed login: \nUsername: " + username + "\nTimestamp: " + DateConverter.format(DateConverter.convertSystemToUTC(LocalDateTime.now())) + " (UTC)\n\n";
                Alert alert = new Alert(Alert.AlertType.ERROR);
                if (locale == Locale.FRENCH || locale == Locale.FRANCE || locale == Locale.CANADA_FRENCH) {
                    alert.setContentText("Nom d'utilisateur ou mot de passe incorrect!");
                }
                else {
                    alert.setContentText("Incorrect Username or Password!");
                }
                alert.show();
            }
            fileWriter.write(fileOutput);
        } catch (IOException e) {
            System.out.println("File error!");
        }

    }

    /**
     * Sets the text of the location Label to the user's ZoneId.
     */
    public void setLocationLabel() {
        ZoneId zoneId = ZoneId.systemDefault();
        locationLabelText.setText(zoneId.toString());
    }

    /**
     * Changes the text of the login window to French if the user's Locale is set that way.
     */
    public void setLanguage() {
        if (locale.getLanguage().equals("fr")) {
            titleLabel.setText("Planification de la connexion");
            usernameLabel.setText("Nom d'utilisateur: ");
            passwordLabel.setText("Mot de passe: ");
            locationLabel.setText("Emplacement: ");
            loginButton.setText("Connexion");
        }
    }

}
