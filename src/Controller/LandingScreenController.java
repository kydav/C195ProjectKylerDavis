package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.text.ParseException;
import java.util.ResourceBundle;
import static main.QueryManager.loggedUser;
import static main.QueryManager.checkAppointmentsIncoming;

public class LandingScreenController {
    @FXML
    private ResourceBundle resources;
    @FXML
    private Button schedulingButton;
    @FXML
    private Button customersButton;
    @FXML
    private Button reportsButton;
    @FXML
    void manageCustomersClicked(ActionEvent event) throws Exception {
        Stage stage = (Stage) customersButton.getScene().getWindow();
        Parent customers = FXMLLoader.load(getClass().getResource("../View/ManageCustomer.fxml"), resources);
        Scene scene = new Scene(customers);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void reportsButtonClicked(ActionEvent event) throws Exception{
        //Stage stage = (Stage) reportsButton.getScene().getWindow();
        //Parent reports = FXMLLoader.load(getClass().getResource("../View/ManageReports.fxml"), resources);
        //Scene scene = new Scene(reports);
        //stage.setScene(scene);
        //stage.show();
    }

    @FXML
    void schedulingButtonClicked(ActionEvent event) throws Exception{
        Stage stage = (Stage) schedulingButton.getScene().getWindow();
        Parent schedules = FXMLLoader.load(getClass().getResource("../View/ManageAppointments.fxml"), resources);
        Scene scene = new Scene(schedules);
        stage.setScene(scene);
        stage.show();
    }
    public void initialize()throws ParseException {
        String[] incomingString = checkAppointmentsIncoming(loggedUser);

        if(incomingString[0] != null){
            System.out.println(incomingString);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resources.getString("appointment.upcomingAppointmentTitle"));
            alert.setHeaderText(resources.getString("appointment.upcomingAppointmentHeader"));
            alert.setContentText(resources.getString("generic.title") + " : "  + incomingString[0] + "\n" +
                resources.getString("generic.name") + " : " + incomingString[1] + "\n" +
                resources.getString("generic.startDate") + " : " + incomingString[2]);
            alert.showAndWait();
        }



    }

}
