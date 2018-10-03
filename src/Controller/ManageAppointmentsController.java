package Controller;

import Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;
import java.util.ResourceBundle;

import static main.QueryManager.deleteTheAppointment;
import static main.QueryManager.getAppointmentTableView;
import static main.QueryManager.getAppointmentsByMonth;
import static main.QueryManager.getAppointmentsByWeek;

public class ManageAppointmentsController {
    public static ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    public static int appointmentToModifyId = -1;
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
    private TableColumn<Appointment, String> dayColumn;
    @FXML
    private TableColumn<Appointment, String> startColumn;
    @FXML
    private TableColumn<Appointment, String> endColumn;
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
    private Button byFutureButton;
    @FXML
    private Button byMonthButton;
    @FXML
    private Button byWeekButton;
    @FXML
    void deleteAppointment(){
        appointmentToModifyIndex = manageAppointmentTableView.getSelectionModel().getSelectedIndex();
        if(appointmentToModifyIndex != -1) {
            try{
                appointmentToModifyId = manageAppointmentTableView.getSelectionModel().getSelectedItem().getAppointmentId();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle(resources.getString("manage.deleteConfirmTitle"));
                alert.setHeaderText(resources.getString("appointment.deleteConfirmHeader"));
                alert.setContentText(resources.getString("manage.deleteConfirmText"));
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK) {
                    int rowsDeleted = deleteTheAppointment(appointmentToModifyId);
                    System.out.println(rowsDeleted + " Appointment Deleted Successfully");
                    populateTableView(getAppointmentTableView());
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
            appointmentToModifyId = manageAppointmentTableView.getSelectionModel().getSelectedItem().getAppointmentId();

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
        appointmentToModifyId = -1;
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
    }@FXML
    void loadFutureAppointments(){
        try {
            populateTableView(getAppointmentTableView());
        }catch(ParseException e){
            e.printStackTrace();
        }
    }
    @FXML
    void loadMonthAppointments(){
        try{
        populateTableView(getAppointmentsByMonth());
        }catch(ParseException e){
            e.printStackTrace();
        }
    }
    @FXML
    void loadWeekAppointments(){
        try{
            populateTableView(getAppointmentsByWeek());

        }catch(ParseException e){
            e.printStackTrace();
        }
    }
    public void populateTableView(ObservableList<Appointment> appointmentListInput){
        try{
            appointmentList = appointmentListInput;
            idColumn.setCellValueFactory(cellData -> cellData.getValue().appointmentIdProperty().asObject());
            nameColumn.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
            titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
            locationColumn.setCellValueFactory(cellData -> cellData.getValue().locationProperty());
            dayColumn.setCellValueFactory(cellData -> cellData.getValue().startDayOfWeekProperty());
            startColumn.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
            endColumn.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());
            manageAppointmentTableView.setItems(appointmentListInput);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error putting data into TableView");
        }
    }
    public void initialize()throws ParseException {
        appointmentList = getAppointmentTableView();
        populateTableView(appointmentList);
    }
}
