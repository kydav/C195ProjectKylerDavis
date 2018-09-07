package Controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import java.util.Optional;
import java.io.IOException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import static main.DBConnection.*;
import Model.Customer;
import Model.Customer.*;

public class NewCustomerController {
    @FXML
    private ResourceBundle resources;
    @FXML
    private TextField customerCityField;
    @FXML
    private TextField customerPostalCodeField;
    @FXML
    private TextField customerNameField;
    @FXML
    private TextField customerPhoneField;
    @FXML
    private TextField customerAddressField;
    @FXML
    private TextField customerAddress2Field;
    @FXML
    private TextField customerCountryField;
    @FXML
    private Button customerSaveButton;
    @FXML
    private Button customerCancelButton;
    @FXML
    void CancelCustomer(ActionEvent event) throws IOException{
        Stage stage = (Stage) customerCancelButton.getScene().getWindow();
        Parent manage = FXMLLoader.load(getClass().getResource("../View/ManageCustomer.fxml"), resources);
        Scene scene = new Scene(manage);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void SaveCustomer(ActionEvent event) {
        String name = customerNameField.getText();
        String phone = customerPhoneField.getText();
        String address = customerAddressField.getText();
        String address2 = customerAddress2Field.getText();
        String city = customerCityField.getText();
        String postalCode = customerPostalCodeField.getText();
        String country = customerCountryField.getText();
        String validCustomer = Customer.validCustomer(name, address, postalCode,phone,city,country);
        if(validCustomer == null){
            Customer newCustomer = new Customer();
            newCustomer.setCustomerName(name);
            newCustomer.setPhone(phone);
            newCustomer.setAddress(address);
            newCustomer.setAddress2(address2);
            newCustomer.setCity(city);
            newCustomer.setPostalCode(postalCode);
            newCustomer.setCountry(country);

        }else{
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle(resources.getString("new.errorTitle"));
            alert.setHeaderText(resources.getString("new.errorHeader"));
            alert.setContentText(validCustomer);
            alert.showAndWait();
        }
    }

}
