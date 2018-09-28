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

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.TimeZone;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.text.DateFormat;


import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.ResourceBundle;

import static Controller.ManageAppointmentsController.appointmentList;
import static Controller.ManageAppointmentsController.appointmentToModifyIndex;
import static main.QueryManager.*;
import Model.Appointment;
import Model.Appointment.*;

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
    private ComboBox<String> startHour;
    @FXML
    private ComboBox<String> startMinutes;
    @FXML
    private ComboBox<String> startPeriod;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ComboBox<String> endHour;
    @FXML
    private ComboBox<String> endMinutes;
    @FXML
    private ComboBox<String> endPeriod;
    @FXML
    private ComboBox<String> typeCombo;

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
    void SaveAppointment(ActionEvent event) throws IOException, ParseException {
        String customer = customerCombo.getValue();
        String user = userCombo.getValue();
        String title = titleField.getText();
        String description = descriptionField.getText();
        String location = locationField.getText();

        String type = typeCombo.getValue();
        String url = urlField.getText();

        LocalDate startLocalDate = startDatePicker.getValue();
        LocalDate endLocalDate = endDatePicker.getValue();

        String startHourString;
        String endHourString;

        if(startPeriod.getValue().equals("PM")){
            startHourString = Integer.toString(Integer.parseInt(startHour.getValue()) + 12);
        }else{
            startHourString = startHour.getValue();
        }
        if(endPeriod.getValue().equals("PM")){
            endHourString = Integer.toString(Integer.parseInt(endHour.getValue()) + 12);
        }else{
            endHourString = endHour.getValue();
        }

        LocalTime startLocalTime = LocalTime.of(Integer.parseInt(startHourString), Integer.parseInt(startMinutes.getValue()));
        LocalDateTime startLocalDateTime = LocalDateTime.of(startLocalDate, startLocalTime);
        LocalTime endLocalTime = LocalTime.of(Integer.parseInt(endHourString), Integer.parseInt(endMinutes.getValue()));
        LocalDateTime endLocalDateTime = LocalDateTime.of(endLocalDate, endLocalTime);

        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime zdtStart = startLocalDateTime.atZone(zid);
        ZonedDateTime zdtEnd = endLocalDateTime.atZone(zid);

        ZonedDateTime utcStart = zdtStart.withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime utcEnd = zdtEnd.withZoneSameInstant(ZoneId.of("UTC"));

        startLocalDateTime = utcStart.toLocalDateTime();
        endLocalDateTime = utcEnd.toLocalDateTime();

        Timestamp startsqlts = Timestamp.valueOf(startLocalDateTime); //Should be 2018-02-08 22:00:00
        Timestamp endsqlts = Timestamp.valueOf(endLocalDateTime);  //Should be 2018-02-08 23:00:00

        String validAppointment = Appointment.validAppointment(title,customer,type,description,user,startsqlts,endsqlts,startLocalDateTime,endLocalDateTime,startLocalTime,endLocalTime);

        boolean appointmentOverLaps = appointmentOverlaps(startsqlts, endsqlts);
        if(validAppointment.equals("")  && appointmentOverLaps){

        }
    }
    public void initialize() {
        ObservableList<String> customerNames = getCustomerNames();
        customerCombo.setItems(customerNames);
        ObservableList<String> userNames = getUserNames();
        userCombo.setItems(userNames);
        ObservableList<String> hourDrop = getHourDrops();
        startHour.setItems(hourDrop);
        endHour.setItems(hourDrop);
        ObservableList<String> minuteDrop = getMinuteDrops();
        startMinutes.setItems(minuteDrop);
        endMinutes.setItems(minuteDrop);
        ObservableList<String> periodDrop = getPeriodDrops();
        startPeriod.setItems(periodDrop);
        endPeriod.setItems(periodDrop);
        typeCombo.setItems(getTypeDrops());

        if(appointmentToModifyIndex != -1){
            try {
                Appointment appointmentToModify = appointmentList.get(appointmentToModifyIndex);
                titleField.setText(appointmentToModify.getTitle());
                descriptionField.setText(appointmentToModify.getDescription());
                locationField.setText(appointmentToModify.getLocation());
                urlField.setText(appointmentToModify.getUrl());
                customerCombo.getSelectionModel().select(customerNames.indexOf(appointmentToModify.getCustomerName()));
                userCombo.getSelectionModel().select(userNames.indexOf(appointmentToModify.getUserName()));
                typeCombo.getSelectionModel().select(appointmentToModify.getType());

                DateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                dt.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date startDate = dt.parse(appointmentToModify.getStart().toString());
                Date endDate = dt.parse(appointmentToModify.getEnd().toString());

                DateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
                DateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");

                String startDateAsString = dateFormatter.format(startDate);
                String startTimeAsString = timeFormatter.format(startDate);

                String endDateAsString = dateFormatter.format(endDate);
                String endTimeAsString = timeFormatter.format(endDate);

                int startYearInt = Integer.parseInt(startDateAsString.substring(0,4));
                int startMonthInt = Integer.parseInt(startDateAsString.substring(5,7));
                int startDayInt = Integer.parseInt(startDateAsString.substring(8,10));
                int endYearInt = Integer.parseInt(endDateAsString.substring(0,4));
                int endMonthInt = Integer.parseInt(endDateAsString.substring(5,7));
                int endDayInt = Integer.parseInt(endDateAsString.substring(8,10));

                String startHourString = startTimeAsString.substring(0,2);
                String startMinuteString = startTimeAsString.substring(3,5);
                String endHourString = endTimeAsString.substring(0,2);
                String endMinuteString = endTimeAsString.substring(3,5);
                if(Integer.parseInt(startHourString) > 12){
                    startHourString = Integer.toString(Integer.parseInt(startHourString)-12);
                    startPeriod.getSelectionModel().select("PM");
                }else{
                    startPeriod.getSelectionModel().select("AM");
                }
                if(Integer.parseInt(endHourString) > 12){
                    endHourString = Integer.toString(Integer.parseInt(endHourString)-12);
                    endPeriod.getSelectionModel().select("PM");
                }else{
                    endPeriod.getSelectionModel().select("AM");
                }
                startDatePicker.setValue(LocalDate.of(startYearInt, startMonthInt, startDayInt));
                endDatePicker.setValue(LocalDate.of(endYearInt,endMonthInt,endDayInt));
                startHour.getSelectionModel().select(startHourString);
                startMinutes.getSelectionModel().select(startMinuteString);
                endHour.getSelectionModel().select(endHourString);
                endMinutes.getSelectionModel().select(endMinuteString);
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("Error loading Appointment");
            }
        }
    }

}

