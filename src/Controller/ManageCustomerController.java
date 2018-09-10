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

public class ManageCustomerController {
    public ObservableList<Customer> customerList = FXCollections.observableArrayList();
    public static ObservableList<String> customerToModify;

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
    void deleteCustomer(ActionEvent event) throws IOException {
        /*customerToModify = manageCustomerTableView.getSelectionModel().getSelectedItem();
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
                    Statement stmt = conn.createStatement();
                    int rows = stmt.executeUpdate(customerToDelete);
                    System.out.println(rows + "Customer Deleted Successfully");
                    populateTableView();
                }
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("Error Deleting Customer");
            }
        }else{
            alertFunction(resources.getString("manage.noSelectedTitle"), resources.getString("manage.noSelectedHeader"));
        }*/
    }
    @FXML
    void editCustomer(ActionEvent event) throws IOException {
        /*customerToModify = manageCustomerTableView.getSelectionModel().getSelectedItem();
        String customerToModifyId = customerToModify.get(0);
        if(customerToModify != null) {
            Stage stage = (Stage) manageCustomerEdit.getScene().getWindow();
            Parent modifyCustomer = FXMLLoader.load(getClass().getResource("../View/NewEditCustomer.fxml"), resources);
            Scene scene = new Scene(modifyCustomer);
            stage.setScene(scene);
            stage.show();
        }else{
            alertFunction(resources.getString("manage.noSelectedTitle"), resources.getString("manage.noSelectedHeader"));
        }*/
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
            String allCustomer =    "SELECT U04EE1.customer.customerid, U04EE1.customer.customerName, " +
                                    "U04EE1.address.addressId, U04EE1.address.address, U04EE1.address.address2,  " +
                                    "U04EE1.city.cityId, U04EE1.address.postalCode, U04EE1.address.phone, " +
                                    "U04EE1.city.city, U04EE1.city.countryId, U04EE1.country.country, U04EE1.customer.active \n" +
                                    "FROM U04EE1.customer \n" +
                                    "JOIN U04EE1.address ON U04EE1.customer.addressid = U04EE1.address.addressid \n" +
                                    "JOIN U04EE1.city ON U04EE1.address.cityid = U04EE1.city.cityid \n" +
                                    "JOIN U04EE1.country ON U04EE1.city.countryid = U04EE1.country.countryid \n" +
                                    "WHERE U04EE1.customer.active = 1;";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(allCustomer);
            while (rs.next()) {
                Customer current = new Customer();
                current.setCustomerId(new SimpleIntegerProperty(Integer.parseInt(rs.getString("customerId"))));
                current.setCustomerName(rs.getString("customerName"));
                current.setAddressId(Integer.parseInt((rs.getString("addressId"))));
                current.setAddress(rs.getString("address"));
                current.setAddress2(rs.getString("address2"));
                current.setCityId(Integer.parseInt((rs.getString("cityId"))));
                current.setPostalCode(rs.getString("postalCode"));
                current.setPhone(rs.getString("phone"));
                current.setCity(rs.getString("city"));
                current.setCountryId(Integer.parseInt((rs.getString("countryId"))));
                current.setCountry(rs.getString("country"));
                current.setActive(Integer.parseInt((rs.getString("active"))));
                customerList.add(current);
            }
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
            /*for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
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
            manageCustomerTableView.setItems(data);*/



        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }
    public void initialize(){
        populateTableView();
    }
}
