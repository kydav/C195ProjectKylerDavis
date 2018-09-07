package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {
    public String loggedInUser;
    @Override
    public void start(Stage primaryStage) throws Exception{
        String userLanguage = Locale.getDefault().getLanguage();
        Locale  userDefault = new Locale(userLanguage);
        Locale.setDefault(userDefault);
        ResourceBundle defaultBundle = ResourceBundle.getBundle("Resources", userDefault);

        Parent root = FXMLLoader.load(getClass().getResource("../View/LoginScreen.fxml"),defaultBundle);
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException, Exception {
        DBConnection.makeConnection();
        launch(args);
        DBConnection.closeConnection();
    }
}
