package Controller;

import Model.Appointment;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class AppointmentsByConsultantController {
    @FXML
    private ResourceBundle resources;
    @FXML
    private TableView<Appointment> AppointmentsByConsultantTableView;
    @FXML
    private TableColumn<Appointment, Integer> idColumn;
    @FXML
    private TableColumn<Appointment, String> nameColumn;
    @FXML
    private TableColumn<Appointment, String> titleColumn;
    @FXML
    private TableColumn<Appointment, String> locationColumn;
    @FXML
    private TableColumn<Appointment, String> dayColumn;
    @FXML
    private TableColumn<Appointment, Timestamp> startColumn;
    @FXML
    private TableColumn<Appointment, Timestamp> endColumn;

    @FXML
    private Button cancelButton;

    @FXML
    void cancelAppointments() {
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

}
