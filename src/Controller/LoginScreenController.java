package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;
import javafx.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;


import static main.DBConnection.*;


public class LoginScreenController {
    @FXML
    private ResourceBundle resources;
    @FXML
    private TextField loginUsername;
    @FXML
    private PasswordField loginPassword;
    @FXML
    private Button loginButton;
    @FXML
    void loginClicked(ActionEvent event) throws IOException, ClassNotFoundException, SQLException, Exception {
        String userName = loginUsername.getText();
        String password = loginPassword.getText();
        if (userName.trim().isEmpty() || password.trim().isEmpty()) {
            alertFunction(resources.getString("login.fieldsEmpty"), resources.getString("login.empty"));
        } else {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT password FROM user WHERE userName = '" + userName + "'");
            if (!rs.next()) {
                alertFunction(resources.getString("login.userNotFound"), resources.getString("login.user") + " " + userName + " " + resources.getString("login.notFound"));
            } else {
                String userPass = rs.getString("password");
                if (userPass.equals(password)) {
                    System.out.println("User login successful");

                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    Parent login = FXMLLoader.load(getClass().getResource("../View/LandingScreen.fxml"));
                    Scene scene = new Scene(login);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    alertFunction(resources.getString("login.incorrectPassword"), resources.getString("login.incorrect") + " " + userName + ". " + resources.getString("login.tryAgain"));
                }
            }
        }
    }
    void alertFunction(String title, String header){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }
}
