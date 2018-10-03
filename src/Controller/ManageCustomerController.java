package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import java.util.Optional;
import java.io.IOException;
import java.util.ResourceBundle;

import Model.Customer;
import static main.QueryManager.getCustomerTableView;
import static main.QueryManager.deleteTheCustomer;

public class ManageCustomerController {
    public static ObservableList<Customer> customerList = FXCollections.observableArrayList();
    public static int customerToModifyIndex = -1;
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
        customerToModifyIndex = manageCustomerTableView.getSelectionModel().getSelectedIndex();
        if(customerToModifyIndex != -1) {
            try{
                int customerToModifyId = customerList.get(manageCustomerTableView.getSelectionModel().getFocusedIndex()).getCustomerId();
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
    void editCustomer(){
        customerToModifyIndex = manageCustomerTableView.getSelectionModel().getSelectedIndex();
        if(customerToModifyIndex != -1) {
            try {
                Stage stage = (Stage) manageCustomerEdit.getScene().getWindow();
                Parent modifyCustomer = FXMLLoader.load(getClass().getResource("../View/NewEditCustomer.fxml"), resources);
                Scene scene = new Scene(modifyCustomer);
                stage.setScene(scene);
                stage.show();
            }catch(IOException e){
                e.printStackTrace();
            }
        }else{
            alertFunction(resources.getString("manage.noSelectedTitle"), resources.getString("manage.noSelectedHeader"));
        }
    }
    @FXML
    void newCustomer(){
        try {
            customerToModifyIndex = -1;
            Stage stage = (Stage) manageCustomerNew.getScene().getWindow();
            Parent newCustomer = FXMLLoader.load(getClass().getResource("../View/NewEditCustomer.fxml"), resources);
            Scene scene = new Scene(newCustomer);
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    void cancelManageCustomer(){
        try {
            Stage stage = (Stage) manageCustomerCancel.getScene().getWindow();
            Parent landing = FXMLLoader.load(getClass().getResource("../View/LandingScreen.fxml"), resources);
            Scene scene = new Scene(landing);
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
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
            idColumn.setCellValueFactory(cellData -> cellData.getValue().customerIdProperty().asObject());
            nameColumn.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
            phoneColumn.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
            cityColumn.setCellValueFactory(cellData -> cellData.getValue().cityProperty());
            postalCodeColumn.setCellValueFactory(cellData -> cellData.getValue().postalCodeProperty());
            countryColumn.setCellValueFactory(cellData -> cellData.getValue().countryProperty());
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
