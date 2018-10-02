package Controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ResourceBundle;

import static main.QueryManager.getTypeDrops;
import static main.QueryManager.getUserNames;

public class ReportScreenController {
    public String consultantName;
    public String appointmentType;
    @FXML
    private ResourceBundle resources;
    @FXML
    private Button appointmentsByConsultantButton;
    @FXML
    private Button appointmentTypesButton;
    @FXML
    private Button inactiveCustomersButton;
    @FXML
    private ComboBox<String> consultantsCombo;
    @FXML
    private ComboBox<String> appointmentTypesCombo;
    @FXML
    private Button cancelButton;

    @FXML
    void cancelReports() {
        try {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            Parent landing = FXMLLoader.load(getClass().getResource("../View/LandingScreen.fxml"), resources);
            Scene scene = new Scene(landing);
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    void getAppointmentsByConsultant() {
        consultantName = consultantsCombo.getSelectionModel().getSelectedItem();
        try {
            Stage stage = (Stage) appointmentsByConsultantButton.getScene().getWindow();
            Parent consultant = FXMLLoader.load(getClass().getResource("../View/AppointmentsByConsultant.fxml"), resources);
            Scene scene = new Scene(consultant);
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    void getAppointmentsByType() {
        appointmentType = appointmentTypesCombo.getSelectionModel().getSelectedItem();
        try {
            Stage stage = (Stage) appointmentTypesButton.getScene().getWindow();
            Parent type = FXMLLoader.load(getClass().getResource("../View/AppointmentsByType.fxml"), resources);
            Scene scene = new Scene(type);
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    void viewInactiveCustomers() {
        try {
            Stage stage = (Stage) inactiveCustomersButton.getScene().getWindow();
            Parent inactive = FXMLLoader.load(getClass().getResource("../View/InactiveCustomers.fxml"), resources);
            Scene scene = new Scene(inactive);
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void initialize(){
        ObservableList<String> userNames = getUserNames();
        consultantsCombo.setItems(userNames);
        appointmentTypesCombo.setItems(getTypeDrops());
    }
}

