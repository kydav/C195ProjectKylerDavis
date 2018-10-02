package Controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

import static main.QueryManager.getTypeDrops;
import static main.QueryManager.getUserIdWithUserName;
import static main.QueryManager.getUserNames;

public class ReportScreenController {
    public  String consultantName;
    public static String appointmentType;
    public static int consultantId;
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
        consultantId = getUserIdWithUserName(consultantName);
        if(consultantName == null){
            alertFunction(resources.getString("reports.noConsultantTitle"), resources.getString("reports.noConsultantHeader"));
        } else{
            try {
                Stage stage = (Stage) appointmentsByConsultantButton.getScene().getWindow();
                Parent consultant = FXMLLoader.load(getClass().getResource("../View/AppointmentsByConsultant.fxml"), resources);
                Scene scene = new Scene(consultant);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    void getAppointmentsByType() {
        appointmentType = appointmentTypesCombo.getSelectionModel().getSelectedItem();
        if(appointmentType == null){
            alertFunction(resources.getString("reports.noTypeTitle"), resources.getString("reports.noTypeHeader"));
        }else {
            try {
                Stage stage = (Stage) appointmentTypesButton.getScene().getWindow();
                Parent type = FXMLLoader.load(getClass().getResource("../View/AppointmentsByType.fxml"), resources);
                Scene scene = new Scene(type);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    void alertFunction(String title, String header){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }
    public void initialize(){
        ObservableList<String> userNames = getUserNames();
        consultantsCombo.setItems(userNames);
        appointmentTypesCombo.setItems(getTypeDrops());
    }
}

