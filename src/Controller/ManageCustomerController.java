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

import java.util.Optional;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Model.Customer;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ResourceBundle;

import static main.DBConnection.*;
import javafx.beans.binding.Binding;
import javafx.scene.control.TableView;

public class ManageCustomerController {
//public class ManageCustomerController implements FocusListener {
    public static ObservableList<String> customerToModify;
    public static Customer customerToModifyTwo;
    private ObservableList<ObservableList> data;
    @FXML
    private TableView<ObservableList> manageCustomerTableView;
    //public void focusGained(FocusEvent e){
    //    manageCustomerEdit.setDisable(false);
    //}
    //public void focusLost(FocusEvent e){
    //    manageCustomerEdit.setDisable(false);
    //}
    @FXML
    private ResourceBundle resources;
    @FXML
    private Button manageCustomerEdit;
    @FXML
    private Button manageCustomerDelete;
    @FXML
    private Button manageCustomerNew;
    @FXML
    private Button manageCustomerCancel;
    @FXML
    void deleteCustomer(ActionEvent event) throws IOException {
        customerToModify = manageCustomerTableView.getSelectionModel().getSelectedItem();
        String customerToModifyId = customerToModify.get(0);

        if(customerToModify != null) {
            try{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle(resources.getString("manage.deleteConfirmTitle"));
                alert.setHeaderText(resources.getString("manage.deleteConfirmHeader"));
                alert.setContentText(resources.getString("manage.deleteConfirmText"));
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK) {
                    String customerToDelete = "UPDATE U04EE1.customer \n" +
                            "SET U04EE1.customer.active = 0 \n" +
                            "WHERE U04EE1.customer.customerId = " + customerToModifyId + ";";
                    System.out.println(customerToDelete);
                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate(customerToDelete);
                    populateTableView();
                }
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("Error Deleting Customer");
            }
        }else{
            alertFunction(resources.getString("manage.noSelectedTitle"), resources.getString("manage.noSelectedHeader"));
        }
    }
    @FXML
    void editCustomer(ActionEvent event) throws IOException {
        customerToModify = manageCustomerTableView.getSelectionModel().getSelectedItem();

        if(customerToModify != null) {
            Stage stage = (Stage) manageCustomerEdit.getScene().getWindow();
            Parent modifyCustomer = FXMLLoader.load(getClass().getResource("ModifyCustomer.fxml"));
            Scene scene = new Scene(modifyCustomer);
            stage.setScene(scene);
            stage.show();
        }else{
            alertFunction(resources.getString("manage.noSelectedTitle"), resources.getString("manage.noSelectedHeader"));
        }
    }
    @FXML
    void newCustomer(ActionEvent event) {

    }
    @FXML
    void cancelManageCustomer(ActionEvent event) throws IOException {
        Stage stage = (Stage) manageCustomerCancel.getScene().getWindow();
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
        data = FXCollections.observableArrayList();
        try{
            String allCustomer =    "SELECT U04EE1.customer.customerid, U04EE1.customer.customerName, U04EE1.address.phone, U04EE1.country.country, U04EE1.address.postalCode  \n" +
                                    "FROM U04EE1.customer \n" +
                                    "JOIN U04EE1.address ON U04EE1.customer.addressid = U04EE1.address.addressid \n" +
                                    "JOIN U04EE1.city ON U04EE1.address.cityid = U04EE1.city.cityid \n" +
                                    "JOIN U04EE1.country ON U04EE1.city.countryid = U04EE1.country.countryid \n" +
                                    "WHERE U04EE1.customer.active = 1;";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(allCustomer);

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                manageCustomerTableView.getColumns().addAll(col);
            }
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }
            manageCustomerTableView.setItems(data);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }
    public void initialize(){
        populateTableView();

    }

}
