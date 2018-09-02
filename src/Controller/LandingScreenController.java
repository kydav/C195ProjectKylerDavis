package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class LandingScreenController {

    @FXML
    private Button schedulingButton;

    @FXML
    private Button customersButton;

    @FXML
    private Button reportsButton;

    @FXML
    void manageCustomersClicked(ActionEvent event) throws Exception {
        Stage stage = (Stage) customersButton.getScene().getWindow();
        Parent customers = FXMLLoader.load(getClass().getResource("../View/ManageCustomer.fxml"));
        Scene scene = new Scene(customers);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void reportsButtonClicked(ActionEvent event) throws Exception{
        Stage stage = (Stage) reportsButton.getScene().getWindow();
        Parent reports = FXMLLoader.load(getClass().getResource("../View/manageReports.fxml"));
        Scene scene = new Scene(reports);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void schedulingButtonClicked(ActionEvent event) throws Exception{
        Stage stage = (Stage) schedulingButton.getScene().getWindow();
        Parent schedules = FXMLLoader.load(getClass().getResource("../View/manageScheduling.fxml"));
        Scene scene = new Scene(schedules);
        stage.setScene(scene);
        stage.show();
    }

}
