package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;

public class ManageCustomerController {

    @FXML
    private TableView<?> manageCustomerTableView;

    @FXML
    private TableColumn<?, ?> tableViewCustomerId;

    @FXML
    private TableColumn<?, ?> tableViewCustomerName;

    @FXML
    private TableColumn<?, ?> tableViewCustomerPhone;

    @FXML
    private TableColumn<?, ?> tableViewCustomerCountry;

    @FXML
    private TableColumn<?, ?> tableViewCustomerPostalCode;

    @FXML
    private Button manageCustomerEdit;

    @FXML
    private Button manageCustomerDelete;

    @FXML
    private Button manageCustomerNew;

    @FXML
    private Button manageCustomerCancel;

    @FXML
    void cancelManageCustomer(ActionEvent event) {

    }

    @FXML
    void deleteCustomer(ActionEvent event) {

    }

    @FXML
    void editCustomer(ActionEvent event) {

    }

    @FXML
    void newCustomer(ActionEvent event) {

    }

}
