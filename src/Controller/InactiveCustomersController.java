package Controller;

import Model.Customer;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
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

public class InactiveCustomersController {
    private ObservableList<Customer> customerList;
    public static int customerToModifyIndex = -1;
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
            customerList = getInactiveCustomers();

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
            inactiveCustomerTableView.setItems(customerList);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error putting data into TableView");
        }
    }
}
