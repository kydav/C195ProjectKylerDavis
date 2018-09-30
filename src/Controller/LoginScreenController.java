package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import java.util.ResourceBundle;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import static main.QueryManager.checkAppointmentsIncoming;
import static main.QueryManager.loggedUser;
import static main.QueryManager.userValidation;

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
    void loginClicked(ActionEvent event) throws Exception {
        String userName = loginUsername.getText();
        String password = loginPassword.getText();
        if (userName.trim().isEmpty() || password.trim().isEmpty()) {
            alertFunction(resources.getString("login.fieldsEmpty"), resources.getString("login.empty"));
        } else {
            String isValidUser = userValidation(userName, password);
            if(isValidUser == "notFound"){
                alertFunction(resources.getString("login.userNotFound"), resources.getString("login.user") + " " + userName + " " + resources.getString("login.notFound"));
            }else{
                if(isValidUser == "authenticated"){
                    System.out.println("User login successful");

                    String[] incomingString = checkAppointmentsIncoming(loggedUser);
                    if(incomingString[0] != null){
                        System.out.println(incomingString);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle(resources.getString("appointment.upcomingAppointmentTitle"));
                        alert.setHeaderText(resources.getString("appointment.upcomingAppointmentHeader"));
                        alert.setContentText(resources.getString("generic.title") + " : "  + incomingString[0] + "\n" +
                                resources.getString("generic.name") + " : " + incomingString[1] + "\n" +
                                resources.getString("generic.startDate") + " : " + incomingString[2]);
                        alert.showAndWait();
                    }
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    Parent login = FXMLLoader.load(getClass().getResource("../View/LandingScreen.fxml"), resources);
                    Scene scene = new Scene(login);
                    stage.setScene(scene);
                    stage.show();
                }else{
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
