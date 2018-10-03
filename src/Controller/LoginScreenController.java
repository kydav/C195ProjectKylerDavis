package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    void loginClicked(){
        String userName = loginUsername.getText();
        String password = loginPassword.getText();
        final String fileName = "src/lib/loginLog.txt";
        String isValidUser;
        if (userName.trim().isEmpty() || password.trim().isEmpty()) {
            alertFunction(resources.getString("login.fieldsEmpty"), resources.getString("login.empty"));
        } else {
            try {
                isValidUser = userValidation(userName, password);
            }catch (ParseException e){
                e.printStackTrace();
                isValidUser = "notfound";
            }
            if(isValidUser == "notFound"){
                alertFunction(resources.getString("login.userNotFound"), resources.getString("login.user") + " " + userName + " " + resources.getString("login.notFound"));
            }else{
                if(isValidUser == "authenticated") {
                    System.out.println("User login successful");
                    BufferedWriter bw = null;
                    FileWriter fw = null;
                    try {
                        fw = new FileWriter(fileName, true);
                        bw = new BufferedWriter(fw);
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        bw.write("User: " +userName + " Logged in on: " + dtf.format(now) + "\n");
                        System.out.println("Done");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (bw != null)
                                bw.close();
                            if (fw != null)
                                fw.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    try {
                        String[] incomingString = checkAppointmentsIncoming(loggedUser);
                        if (incomingString[0] != null) {
                            System.out.println(incomingString);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle(resources.getString("appointment.upcomingAppointmentTitle"));
                            alert.setHeaderText(resources.getString("appointment.upcomingAppointmentHeader"));
                            alert.setContentText(resources.getString("generic.title") + " : " + incomingString[0] + "\n" +
                                    resources.getString("generic.name") + " : " + incomingString[1] + "\n" +
                                    resources.getString("generic.startDate") + " : " + incomingString[2]);
                            alert.showAndWait();
                        }
                    }catch(ParseException e){
                        e.printStackTrace();
                    }
                    try {
                        Stage stage = (Stage) loginButton.getScene().getWindow();
                        Parent login = FXMLLoader.load(getClass().getResource("../View/LandingScreen.fxml"), resources);
                        Scene scene = new Scene(login);
                        stage.setScene(scene);
                        stage.show();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
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
