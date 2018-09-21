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
import javafx.scene.control.DatePicker;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.LocalDate;
import java.time.LocalDateTime;


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
    private DatePicker startDatePicker;
    @FXML
    private ComboBox<?> startHour;
    @FXML
    private ComboBox<?> startHours;
    @FXML
    private ComboBox<?> startPeriod;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ComboBox<?> endHour;
    @FXML
    private ComboBox<?> endMinutes;
    @FXML
    private ComboBox<?> endPeriod;

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
        String title = titleField.getText();
        String description = descriptionField.getText();
        String location = locationField.getText();
        String url = urlField.getText();
        String customer = customerCombo.getValue();
        String user = userCombo.getValue();
    }
    public void initialize() {
        ObservableList<String> customerNames = getCustomerNames();
        customerCombo.setItems(customerNames);
        ObservableList<String> userNames = getUserNames();
        userCombo.setItems(userNames);

        if(appointmentToModifyIndex != -1){
            try {
                Appointment appointmentToModify = appointmentList.get(appointmentToModifyIndex);
                titleField.setText(appointmentToModify.getTitle());
                descriptionField.setText(appointmentToModify.getDescription());
                locationField.setText(appointmentToModify.getLocation());
                urlField.setText(appointmentToModify.getUrl());
                customerCombo.getSelectionModel().select(customerNames.indexOf(appointmentToModify.getCustomerName()));
                userCombo.getSelectionModel().select(userNames.indexOf(appointmentToModify.getUserName()));
                System.out.println(appointmentToModify.getStart());
                System.out.println(appointmentToModify.getStartDate());
                System.out.println(appointmentToModify.getStartTime());
                System.out.println(appointmentToModify.getEnd());
                System.out.println(appointmentToModify.getEndDate());
                System.out.println(appointmentToModify.getEndTime());
                //SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                //Date date = dt.parse(appointmentToModify.getStartDate().toString());
                //System.out.println(date);

                Date input = appointmentToModify.getStartDate();
                System.out.println(input);
                Instant instant = input.toInstant();
                ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
                LocalDate date = zdt.toLocalDate();
                System.out.println(date);



            }catch(Exception e){
                e.printStackTrace();
                System.out.println("Error loading Appointment");
            }
        }
    }

}

