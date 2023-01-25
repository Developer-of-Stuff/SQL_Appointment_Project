package project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import project.data.Datasource;

/**
 * Contains the main method. Launches the login window as well as opens and closes the connection with the database.
 *
 * @author Alexander Gulley
 */
public class Main extends Application {

    /**
     * Opens the login window and opens the connection to the database.
     *
     * @param primaryStage the stage for the login window
     * @throws Exception IOException when the FXMLLoader cannot load the specified file
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        if (Datasource.open()) {
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("gui/loginPage.fxml")));
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        }
    }

    /**
     * Closes the connection to the database. Called on program exit.
     */
    @Override
    public void stop() {
        Datasource.close();
    }

    /**
     * Main method.
     * <br><br>
     * Javadoc Location:
     * <br>
     * C195Project/doc
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
