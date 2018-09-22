package Controller;


import Model.Appointment;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.util.Date;

import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

import static main.QueryManager.deleteTheCustomer;
import static main.QueryManager.getAppointmentTableView;

public class ManageAppointmentsController {
    public static ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    public static int appointmentToModifyIndex = -1;

    @FXML
    private TableView<Appointment> manageAppointmentTableView;
    @FXML
    private TableColumn<Appointment, Integer> idColumn;
    @FXML
    private TableColumn<Appointment, String> nameColumn;
    @FXML
    private TableColumn<Appointment, String> titleColumn;
    @FXML
    private TableColumn<Appointment, String> locationColumn;
    @FXML
    private TableColumn<Appointment, Timestamp> startColumn;
    @FXML
    private TableColumn<Appointment, Timestamp> endColumn;
    @FXML
    private ResourceBundle resources;
    @FXML
    private Button manageAppointmentEdit;
    @FXML
    private Button manageAppointmentDelete;
    @FXML
    private Button manageAppointmentNew;
    @FXML
    private Button manageAppointmentCancel;
    @FXML
    void deleteAppointment(){
        int appointmentToModifyId = appointmentList.get(manageAppointmentTableView.getSelectionModel().getFocusedIndex()).getCustomerId();
        if(appointmentToModifyId != 0) {
            try{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle(resources.getString("manage.deleteConfirmTitle"));
                alert.setHeaderText(resources.getString("manage.deleteConfirmHeader"));
                alert.setContentText(resources.getString("manage.deleteConfirmText"));
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK) {
                    int rowsDeleted = deleteTheCustomer(appointmentToModifyId);
                    System.out.println(rowsDeleted + " Appointment Deleted Successfully");
                    populateTableView();
                }
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("Error Deleting Appointment");
            }
        }else{
            alertFunction(resources.getString("manage.noSelectedTitle"), resources.getString("manage.noSelectedHeader"));
        }
    }
    @FXML
    void editAppointment(ActionEvent event) throws IOException {
        appointmentToModifyIndex = manageAppointmentTableView.getSelectionModel().getSelectedIndex();
        if(appointmentToModifyIndex != -1) {
            Stage stage = (Stage) manageAppointmentEdit.getScene().getWindow();
            Parent modifyCustomer = FXMLLoader.load(getClass().getResource("../View/NewEditAppointment.fxml"), resources);
            Scene scene = new Scene(modifyCustomer);
            stage.setScene(scene);
            stage.show();
        }else{
            alertFunction(resources.getString("manage.noSelectedTitle"), resources.getString("manage.noSelectedHeader"));
        }
    }
    @FXML
    void newAppointment(ActionEvent event) throws IOException{
        appointmentToModifyIndex = -1;
        Stage stage = (Stage) manageAppointmentNew.getScene().getWindow();
        Parent newCustomer = FXMLLoader.load(getClass().getResource("../View/NewEditAppointment.fxml"), resources);
        Scene scene = new Scene(newCustomer);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void cancelManageAppointment(ActionEvent event) throws IOException {
        Stage stage = (Stage) manageAppointmentCancel.getScene().getWindow();
        Parent landing = FXMLLoader.load(getClass().getResource("../View/LandingScreen.fxml"), resources);
        Scene scene = new Scene(landing);
        stage.setScene(scene);
        stage.show();
    }
    void alertFunction(String title, String header){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.showAndWait();
    }
    public void populateTableView(){
        try{
            appointmentList = getAppointmentTableView();

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
            startColumn.setCellValueFactory(cellData -> {
                return new ReadOnlyObjectWrapper(cellData.getValue().getStart());
                //return new ReadOnlyObjectWrapper(cellData.getValue().getStart());
            });
            endColumn.setCellValueFactory(cellData -> {
                return new ReadOnlyObjectWrapper(cellData.getValue().getEnd());
            });
            manageAppointmentTableView.setItems(appointmentList);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error putting data into TableView");
        }
    }
    public void initialize(){
        populateTableView();
    }
}
