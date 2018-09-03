package Controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.TableColumn.CellDataFeatures;

import java.io.IOException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static main.DBConnection.*;

public class ManageCustomerController {
    private ObservableList<ObservableList> data;
    @FXML
    private TableView<ObservableList> manageCustomerTableView;
    @FXML
    private Button manageCustomerEdit;
    @FXML
    private Button manageCustomerDelete;
    @FXML
    private Button manageCustomerNew;
    @FXML
    private Button manageCustomerCancel;
    public void populateTableView(){
        data = FXCollections.observableArrayList();
        try{
            String allCustomer = "SELECT custo.customerid, custo.customerName, address.phone, country.country, address.postalCode  \n" +
                    "FROM U04EE1.customer as custo \n" +
                    "join U04EE1.address as address on custo.addressid = address.addressid \n" +
                    "join U04EE1.city as city on address.cityid = city.cityid \n" +
                    "join U04EE1.country as country on city.countryid = country.countryid;";
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
    @FXML
    void cancelManageCustomer(ActionEvent event) throws IOException {
        Stage stage = (Stage) manageCustomerCancel.getScene().getWindow();
        Parent landing = FXMLLoader.load(getClass().getResource("../View/LandingScreen.fxml"));
        Scene scene = new Scene(landing);
        stage.setScene(scene);
        stage.show();
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
    public void initialize(){
        populateTableView();
        manageCustomerDelete.setDisable(true);
        manageCustomerEdit.setDisable(true);
    }

}
