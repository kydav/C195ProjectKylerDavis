package Controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.awt.event.ActionEvent;

import static main.QueryManager.getTypeDrops;
import static main.QueryManager.getUserNames;

public class ReportScreenController {

    @FXML
    private Button appointmentsByConsultantButton;
    @FXML
    private Button appointmentTypesButton;
    @FXML
    private Button inactiveCustomersButton;
    @FXML
    private ComboBox<String> consultantsCombo;
    @FXML
    private ComboBox<String> appointmentTypesCombo;
    @FXML
    void getAppointmentsByConsultant(ActionEvent event) {

    }
    @FXML
    void getAppointmentsByType(ActionEvent event) {

    }
    @FXML
    void viewInactiveCustomers(ActionEvent event) {

    }
    public void initialize(){
        ObservableList<String> userNames = getUserNames();
        consultantsCombo.setItems(userNames);
        appointmentTypesCombo.setItems(getTypeDrops());
    }
}

