package Controller;

import javafx.beans.property.*;
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

import static main.DBConnection.*;
import Model.Customer;
import static main.QueryManager.getCustomerTableView;
import static main.QueryManager.deleteTheCustomer;

public class ManageCustomerController {
    public static ObservableList<Customer> customerList = FXCollections.observableArrayList();
    public static int customerToModifyIndex = -1;
    public static ObservableList<String> customerToModify;
    public static Optional<ButtonType> result;

    @FXML
    private TableView<Customer> manageCustomerTableView;
    @FXML
    private TableColumn<Customer, Integer> idColumn;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, String> phoneColumn;
    @FXML
    private TableColumn<Customer, String> cityColumn;
    @FXML
    private TableColumn<Customer, String> postalCodeColumn;
    @FXML
    private TableColumn<Customer, String> countryColumn;
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
    void deleteCustomer(){
        int customerToModifyId = customerList.get(manageCustomerTableView.getSelectionModel().getFocusedIndex()).getCustomerId();
        if(customerToModifyId != 0) {
            try{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle(resources.getString("manage.deleteConfirmTitle"));
                alert.setHeaderText(resources.getString("manage.deleteConfirmHeader"));
                alert.setContentText(resources.getString("manage.deleteConfirmText"));
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK) {
                    int rowsDeleted = deleteTheCustomer(customerToModifyId);
                    System.out.println(rowsDeleted + " Customer Deleted Successfully");
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
        customerToModifyIndex = manageCustomerTableView.getSelectionModel().getSelectedIndex();
        if(customerToModifyIndex != -1) {
            Stage stage = (Stage) manageCustomerEdit.getScene().getWindow();
            Parent modifyCustomer = FXMLLoader.load(getClass().getResource("../View/NewEditCustomer.fxml"), resources);
            Scene scene = new Scene(modifyCustomer);
            stage.setScene(scene);
            stage.show();
        }else{
            alertFunction(resources.getString("manage.noSelectedTitle"), resources.getString("manage.noSelectedHeader"));
        }
    }
    @FXML
    void newCustomer(ActionEvent event) throws IOException{
        customerToModify = null;
        Stage stage = (Stage) manageCustomerNew.getScene().getWindow();
        Parent newCustomer = FXMLLoader.load(getClass().getResource("../View/NewEditCustomer.fxml"), resources);
        Scene scene = new Scene(newCustomer);
        stage.setScene(scene);
        stage.show();
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
        try{
            customerList = getCustomerTableView();
            idColumn.setCellValueFactory(cellData -> {
                  return new ReadOnlyObjectWrapper(cellData.getValue().getCustomerId());
            });
            nameColumn.setCellValueFactory(cellData -> {
                return new ReadOnlyStringWrapper(cellData.getValue().getCustomerName());
            });
            phoneColumn.setCellValueFactory(cellData -> {
                return new ReadOnlyStringWrapper(cellData.getValue().getPhone());
            });
            cityColumn.setCellValueFactory(cellData -> {
                return new ReadOnlyStringWrapper(cellData.getValue().getCity());
            });
            postalCodeColumn.setCellValueFactory(cellData -> {
                return new ReadOnlyObjectWrapper(cellData.getValue().getPostalCode());
            });
            countryColumn.setCellValueFactory(cellData -> {
                return new ReadOnlyStringWrapper(cellData.getValue().getCountry());
            });
            manageCustomerTableView.setItems(customerList);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error putting data into TableView");
        }
    }
    public void initialize(){
        populateTableView();
    }
}