package Controller;

import Model.Customer;
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

import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

import static main.QueryManager.deleteTheCustomer;
import static main.QueryManager.getCustomerTableView;

public class ManageAppointmentsController {
    public static ObservableList<Customer> appointmentList = FXCollections.observableArrayList();
    public static int appointmentToModifyIndex = -1;

    @FXML
    private TableView<Customer> manageAppointmentTableView;
    @FXML
    private TableColumn<Customer, Integer> idColumn;
    @FXML
    private TableColumn<Customer, String> customerNameColumn;
    @FXML
    private TableColumn<Customer, String> titleColumn;
    @FXML
    private TableColumn<Customer, String> locationColumn;
    @FXML
    private TableColumn<Customer, String> startColumn;
    @FXML
    private TableColumn<Customer, String> endColumn;
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
            appointmentList = getCustomerTableView();
            idColumn.setCellValueFactory(cellData -> {
                  return new ReadOnlyObjectWrapper(cellData.getValue().getCustomerId());
            });
            customerNameColumn.setCellValueFactory(cellData -> {
                return new ReadOnlyStringWrapper(cellData.getValue().getPhone());
            });
            titleColumn.setCellValueFactory(cellData -> {
                return new ReadOnlyStringWrapper(cellData.getValue().getCity());
            });
            locationColumn.setCellValueFactory(cellData -> {
                return new ReadOnlyObjectWrapper(cellData.getValue().getPostalCode());
            });
            startColumn.setCellValueFactory(cellData -> {
                return new ReadOnlyStringWrapper(cellData.getValue().getCountry());
            });
            endColumn.setCellValueFactory(cellData -> {
                return new ReadOnlyStringWrapper(cellData.getValue().getCustomerName());
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
