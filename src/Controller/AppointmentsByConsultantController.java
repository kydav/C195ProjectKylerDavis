package Controller;

import Model.Appointment;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import static Controller.ReportScreenController.consultantId;
import static main.QueryManager.getAppointmentsByConsultant;

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
            Parent landing = FXMLLoader.load(getClass().getResource("../View/ReportScreen.fxml"), resources);
            Scene scene = new Scene(landing);
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void initialize(){
        try{
            ObservableList<Appointment>  appointmentByConsultantList = getAppointmentsByConsultant(consultantId);
            idColumn.setCellValueFactory(cellData -> {
                return new ReadOnlyObjectWrapper(cellData.getValue().getAppointmentId());
            });
            nameColumn.setCellValueFactory(cellData -> {
                return new ReadOnlyStringWrapper(cellData.getValue().getCustomerName());
            });
            titleColumn.setCellValueFactory(cellData -> {
                return new ReadOnlyStringWrapper(cellData.getValue().getTitle());
            });
            locationColumn.setCellValueFactory(cellData -> {
                return new ReadOnlyStringWrapper(cellData.getValue().getLocation());
            });
            dayColumn.setCellValueFactory(cellData -> {
                return new ReadOnlyStringWrapper(cellData.getValue().getStartDayOfWeek());
            });
            startColumn.setCellValueFactory(cellData -> {
                return new ReadOnlyObjectWrapper(cellData.getValue().getStartDate());
            });
            endColumn.setCellValueFactory(cellData -> {
                return new ReadOnlyObjectWrapper(cellData.getValue().getEndDate());
            });
            AppointmentsByConsultantTableView.setItems(appointmentByConsultantList);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error putting data into TableView");
        }

    }

}
