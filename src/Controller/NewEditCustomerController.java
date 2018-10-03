package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


import java.io.IOException;
import java.util.ResourceBundle;

import static Controller.ManageCustomerController.customerToModifyIndex;
import static Controller.ManageCustomerController.customerList;
import static main.QueryManager.*;
import Model.Customer;

public class NewEditCustomerController {
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
    void CancelCustomer(){
        try {
            customerToModifyIndex = -1;
            Stage stage = (Stage) customerCancelButton.getScene().getWindow();
            Parent manage = FXMLLoader.load(getClass().getResource("../View/ManageCustomer.fxml"), resources);
            Scene scene = new Scene(manage);
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            System.out.println("Error Cancelling out of customer:" + e);
        }
    }
    @FXML
    void SaveCustomer(ActionEvent event) throws IOException{
        String name = customerNameField.getText();
        String phone = customerPhoneField.getText();
        String address = customerAddressField.getText();
        String address2 = customerAddress2Field.getText();
        String city = customerCityField.getText();
        String postalCode = customerPostalCodeField.getText();
        String country = customerCountryField.getText();
        String validCustomer = Customer.validCustomer(name,address,postalCode,phone,city,country);
        boolean customerTableChange = false;
        boolean addressTableChange = false;
        boolean cityChange = false;
        boolean countryChange = false;
        if(validCustomer != null){
            if(customerToModifyIndex == -1){
                int newCountryId = insertCountry(country);
                int newCityId = insertCity(city,newCountryId);
                int newAddressId = insertAddress(address,address2,newCityId,postalCode,phone);
                int insertedCustomerRows = insertCustomer(name,newAddressId);
                System.out.println(insertedCustomerRows);
                Stage stage = (Stage) customerSaveButton.getScene().getWindow();
                Parent manage = FXMLLoader.load(getClass().getResource("../View/ManageCustomer.fxml"), resources);
                Scene scene = new Scene(manage);
                stage.setScene(scene);
                stage.show();
            }else {
                Customer editCustomer = customerList.get(customerToModifyIndex);

                if (!name.equals(editCustomer.getCustomerName())) {
                    editCustomer.setCustomerName(name);
                    customerTableChange = true;
                }
                if (!phone.equals(editCustomer.getPhone())) {
                    editCustomer.setPhone(phone);
                    addressTableChange = true;
                }
                if (!address.equals(editCustomer.getAddress())) {
                    editCustomer.setAddress(address);
                    addressTableChange = true;
                }
                if (!address2.equals(editCustomer.getAddress2())) {
                    editCustomer.setAddress2(address2);
                    addressTableChange = true;
                }
                if (!city.equals(editCustomer.getCity())) {
                    editCustomer.setCity(city);
                    cityChange = true;
                }
                if (!postalCode.equals(editCustomer.getPostalCode())) {
                    editCustomer.setPostalCode(postalCode);
                    addressTableChange = true;
                }
                if (!country.equals(editCustomer.getCountry())) {
                    editCustomer.setCountry(country);
                    countryChange = true;
                }
                if (cityChange && countryChange == false) {
                    int updateCity = insertCity(editCustomer.getCity(),editCustomer.getCountryId());
                    int addressUpdate = addressUpdate(updateCity,editCustomer.getAddressId());
                    System.out.println(updateCity + "is new ID Record updated " + addressUpdate);
                } else if (cityChange && countryChange) {
                    int updateCountry = insertCountry(editCustomer.getCountry());
                    int updatedCity = insertCity(editCustomer.getCity(),updateCountry);
                    int addressUpdate = addressUpdate(updatedCity,editCustomer.getAddressId());
                    System.out.println(updatedCity + "Rows edited and country Id:" +updateCountry + "and addresses updated: " + addressUpdate);
                }
                if (customerTableChange) {
                    int updateCustomerTable = updateCustomerTable(editCustomer.getCustomerName(), editCustomer.getCustomerId());
                    System.out.println(updateCustomerTable + "Record updated");
                }
                if (addressTableChange) {
                    int updateAddressTable = updateAddressTable(editCustomer.getAddressId(), editCustomer.getPhone(), editCustomer.getAddress(), editCustomer.getAddress2(), editCustomer.getPostalCode());
                    System.out.println(updateAddressTable + "Record updated");
                }

                Stage stage = (Stage) customerSaveButton.getScene().getWindow();
                Parent manage = FXMLLoader.load(getClass().getResource("../View/ManageCustomer.fxml"), resources);
                Scene scene = new Scene(manage);
                stage.setScene(scene);
                stage.show();
                customerToModifyIndex = -1;
            }
        }else{
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle(resources.getString("new.errorTitle"));
            alert.setHeaderText(resources.getString("new.errorHeader"));
            alert.setContentText(validCustomer);
            alert.showAndWait();
        }
    }

    public void initialize() {
        if(customerToModifyIndex != -1){
            try {
                Customer customerToModify = customerList.get(customerToModifyIndex);
                customerNameField.setText(customerToModify.getCustomerName());
                customerPhoneField.setText(customerToModify.getPhone());
                customerAddressField.setText(customerToModify.getAddress());
                customerAddress2Field.setText(customerToModify.getAddress2());
                customerCityField.setText(customerToModify.getCity());
                customerPostalCodeField.setText(customerToModify.getPostalCode());
                customerCountryField.setText(customerToModify.getCountry());
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("Error loading customer");
            }
        }
    }
}
