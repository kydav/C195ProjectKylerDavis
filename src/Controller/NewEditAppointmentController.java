package Controller;

import Model.Customer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.ResourceBundle;

import static Controller.ManageAppointmentsController.appointmentList;
import static Controller.ManageAppointmentsController.appointmentToModifyIndex;
import static main.QueryManager.*;
import Model.Appointment;

public class NewEditAppointmentController {
    @FXML
    private ResourceBundle resources;
    @FXML
    private Button appointmentSaveButton;

    @FXML
    private Button appointmentCancelButton;

    @FXML
    private ComboBox<String> customerCombo;

    @FXML
    private ComboBox<String> userCombo;

    @FXML
    private TextField titleField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField locationField;

    @FXML
    private TextField urlField;

    @FXML
    void CancelAppointment(ActionEvent event) throws IOException{
        try {
            Stage stage = (Stage) appointmentCancelButton.getScene().getWindow();
            Parent manage = FXMLLoader.load(getClass().getResource("../View/ManageAppointments.fxml"), resources);
            Scene scene = new Scene(manage);
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            System.out.println("Error Cancelling out of customer:" + e);
        }
    }

    @FXML
    void SaveAppointment(ActionEvent event) throws IOException{

    }
    public void initialize() {
        ObservableList<String> customerNames = getCustomerNames();
        customerCombo.setItems(customerNames);

        if(appointmentToModifyIndex != -1){
            try {
                Appointment appointmentToModify = appointmentList.get(appointmentToModifyIndex);
                titleField.setText(appointmentToModify.getTitle());
                descriptionField.setText(appointmentToModify.getDescription());
                locationField.setText(appointmentToModify.getLocation());
                urlField.setText(appointmentToModify.getUrl());

            }catch(Exception e){
                e.printStackTrace();
                System.out.println("Error loading Appointment");
            }
        }
    }

}

