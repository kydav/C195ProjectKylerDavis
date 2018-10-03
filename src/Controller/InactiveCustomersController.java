package Controller;

import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

import static main.QueryManager.getInactiveCustomers;
import static Controller.ManageCustomerController.customerToModifyIndex;

public class InactiveCustomersController {
    public static ObservableList<Customer> customerListInactive = FXCollections.observableArrayList();
    //public static int customerToModifyIndex = -1;
    public static boolean isFromInactiveCustomersController;
    @FXML
    private ResourceBundle resources;
    @FXML
    private TableView<Customer> inactiveCustomerTableView;
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
    private Button editInactive;
    @FXML
    private Button cancelButton;
    @FXML
    void cancelInactive() {
        try {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            Parent landing = FXMLLoader.load(getClass().getResource("../View/ReportScreen.fxml"), resources);
            Scene scene = new Scene(landing);
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    void editInactive() {
        isFromInactiveCustomersController = true;
        customerToModifyIndex = inactiveCustomerTableView.getSelectionModel().getSelectedIndex();
        if(customerToModifyIndex != -1) {
            try {
                Stage stage = (Stage) editInactive.getScene().getWindow();
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
    void alertFunction(String title, String header){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }
    public void initialize(){
        try{
            customerListInactive = getInactiveCustomers();

            idColumn.setCellValueFactory(cellData -> cellData.getValue().customerIdProperty().asObject());
            nameColumn.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
            phoneColumn.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
            cityColumn.setCellValueFactory(cellData -> cellData.getValue().cityProperty());
            postalCodeColumn.setCellValueFactory(cellData -> cellData.getValue().postalCodeProperty());
            countryColumn.setCellValueFactory(cellData -> cellData.getValue().countryProperty());
            inactiveCustomerTableView.setItems(customerListInactive);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error putting data into TableView");
        }
    }
}
