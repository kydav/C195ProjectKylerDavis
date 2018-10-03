package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ResourceBundle;
import static main.QueryManager.loggedUser;

public class LandingScreenController {
    @FXML
    private ResourceBundle resources;
    @FXML
    private Button schedulingButton;
    @FXML
    private Button customersButton;
    @FXML
    private Button reportsButton;
    @FXML
    private Text userName;
    @FXML
    void manageCustomersClicked(){
        try {
            Stage stage = (Stage) customersButton.getScene().getWindow();
            Parent customers = FXMLLoader.load(getClass().getResource("../View/ManageCustomer.fxml"), resources);
            Scene scene = new Scene(customers);
            stage.setScene(scene);
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void reportsButtonClicked(){
        try {
            Stage stage = (Stage) reportsButton.getScene().getWindow();
            Parent reports = FXMLLoader.load(getClass().getResource("../View/ReportScreen.fxml"), resources);
            Scene scene = new Scene(reports);
            stage.setScene(scene);
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void schedulingButtonClicked(){
        try {
            Stage stage = (Stage) schedulingButton.getScene().getWindow();
            Parent schedules = FXMLLoader.load(getClass().getResource("../View/ManageAppointments.fxml"), resources);
            Scene scene = new Scene(schedules);
            stage.setScene(scene);
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void initialize(){
        userName.setText(loggedUser);
    }
}
