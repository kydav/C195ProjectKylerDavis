package Controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.LocalDateTime;
import java.util.TimeZone;
import java.time.LocalTime;
import java.text.DateFormat;

import javafx.event.ActionEvent;
import javafx.beans.property.SimpleIntegerProperty;
import java.io.IOException;
import java.util.ResourceBundle;

import static Controller.ManageAppointmentsController.appointmentList;
import static Controller.ManageAppointmentsController.appointmentToModifyIndex;
import static main.QueryManager.*;
import Model.Appointment;

public class NewEditAppointmentController {
    Appointment appointmentToModify = new Appointment();
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
    private TextField contactField;
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
        int appointmentId = appointmentToModify.getAppointmentId();
        String customer = customerCombo.getValue();
        int customerId =customerCombo.getSelectionModel().getSelectedIndex() + 1;
        String user = userCombo.getValue();
        int userId = userCombo.getSelectionModel().getSelectedIndex() + 1;
        String title = titleField.getText();
        String description = descriptionField.getText();
        String contact = contactField.getText();
        String location = locationField.getText();
        String type = typeCombo.getValue();
        String url = urlField.getText();
        if(startDatePicker.getValue() == null || endDatePicker.getValue() == null || startHour.getValue() == null ||
                endHour.getValue() == null || startMinutes.getValue() == null || endMinutes.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resources.getString("newAppointment.missingDatesTitle"));
            alert.setHeaderText(resources.getString("newAppointment.missingDatesHeader"));
            alert.setContentText(resources.getString("newAppointment.missingDatesMessage"));
            alert.showAndWait();
        }else {
            LocalDate startLocalDate = startDatePicker.getValue();
            LocalDate endLocalDate = endDatePicker.getValue();
            String startHourString = "";
            String endHourString = "";

            if (startPeriod.getValue().equals("PM")) {
                startHourString = Integer.toString(Integer.parseInt(startHour.getValue()) + 12);
            } else {
                startHourString = startHour.getValue();
            }
            if (endPeriod.getValue().equals("PM")) {
                endHourString = Integer.toString(Integer.parseInt(endHour.getValue()) + 12);
            } else {
                endHourString = endHour.getValue();
            }
            if (Integer.parseInt(startHourString) == 24 || Integer.parseInt(endHourString) == 24) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(resources.getString("newAppointment.midnightTitle"));
                alert.setHeaderText(resources.getString("newAppointment.midnightHeader"));
                alert.setContentText(resources.getString("newAppointment.midnightMessage"));
                alert.showAndWait();
            } else {
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
                String validAppointment = Appointment.validAppointment(title, customer, user, type, description, startsqlts, endsqlts, startLocalDateTime, endLocalDateTime, startLocalTime, endLocalTime);

                boolean appointmentOverLaps = appointmentOverlaps(appointmentToModifyIndex, startsqlts, endsqlts);
                if (validAppointment.equals("") && !appointmentOverLaps) {
                    Appointment appointmentToSave = new Appointment();
                    appointmentToSave.setCustomerName(customer);
                    appointmentToSave.setCustomerId(new SimpleIntegerProperty(customerId));
                    appointmentToSave.setUserName(user);
                    appointmentToSave.setUserId(new SimpleIntegerProperty(userId));
                    appointmentToSave.setTitle(title);
                    appointmentToSave.setDescription(description);
                    appointmentToSave.setLocation(location);
                    appointmentToSave.setContact(contact);
                    appointmentToSave.setType(type);
                    appointmentToSave.setUrl(url);
                    appointmentToSave.setStart(startsqlts);
                    appointmentToSave.setEnd(endsqlts);
                    appointmentToSave.setStartLocalDateTime(startLocalDateTime);
                    appointmentToSave.setEndLocalDateTime(endLocalDateTime);
                    if (appointmentToModifyIndex == -1) {
                        int rowsInserted = insertAppointment(appointmentToSave.getCustomerId(), appointmentToSave.getUserId(), appointmentToSave.getTitle(), appointmentToSave.getDescription(),
                                appointmentToSave.getLocation(), appointmentToSave.getContact(), appointmentToSave.getType(), appointmentToSave.getUrl(), appointmentToSave.getStart(), appointmentToSave.getEnd());
                        System.out.println("Inserted Rows: " + rowsInserted);
                        Stage stage = (Stage) appointmentCancelButton.getScene().getWindow();
                        Parent manage = FXMLLoader.load(getClass().getResource("../View/ManageAppointments.fxml"), resources);
                        Scene scene = new Scene(manage);
                        stage.setScene(scene);
                        stage.show();
                    } else {
                        appointmentToSave.setAppointmentId(new SimpleIntegerProperty(appointmentId));
                        int rowsUpdated = updateAppointment(appointmentToSave.getAppointmentId(), appointmentToSave.getCustomerId(), appointmentToSave.getUserId(), appointmentToSave.getTitle(), appointmentToSave.getDescription(),
                                appointmentToSave.getLocation(), appointmentToSave.getContact(), appointmentToSave.getType(), appointmentToSave.getUrl(), appointmentToSave.getStart(), appointmentToSave.getEnd());
                        System.out.println("Updated rows:" + rowsUpdated);
                        Stage stage = (Stage) appointmentCancelButton.getScene().getWindow();
                        Parent manage = FXMLLoader.load(getClass().getResource("../View/ManageAppointments.fxml"), resources);
                        Scene scene = new Scene(manage);
                        stage.setScene(scene);
                        stage.show();
                    }
                } else if (!validAppointment.equals("")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle(resources.getString("appointment.invalidTitle"));
                    alert.setHeaderText(resources.getString("appointment.invalidHeader"));
                    alert.setContentText(validAppointment);
                    alert.showAndWait();
                } else if (appointmentOverLaps) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle(resources.getString("appointment.overlapsTitle"));
                    alert.setHeaderText(resources.getString("appointment.overlapsHeader"));
                    alert.setContentText(resources.getString("appointment.overlapsMessage"));
                    alert.showAndWait();
                }
            }
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
                appointmentToModify = appointmentList.get(appointmentToModifyIndex);
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

